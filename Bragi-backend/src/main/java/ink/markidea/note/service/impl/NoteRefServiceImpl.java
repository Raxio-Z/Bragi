package ink.markidea.note.service.impl;

import ink.markidea.note.dao.NoteRefRepository;
import ink.markidea.note.dao.NoteRepository;
import ink.markidea.note.entity.NoteDo;
import ink.markidea.note.entity.NoteRefDo;
import ink.markidea.note.entity.resp.ServerResponse;
import ink.markidea.note.entity.vo.NoteVo;
import ink.markidea.note.entity.vo.NotebookVo;
import ink.markidea.note.entity.vo.RefGraphVo;
import ink.markidea.note.service.INoteRefService;
import ink.markidea.note.service.INoteService;
import ink.markidea.note.util.GitUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

@Service
@Slf4j
public class NoteRefServiceImpl implements INoteRefService {

    @Autowired
    private INoteService noteService;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private NoteRefRepository noteRefRepository;

    @Override
    public ServerResponse deleteNoteRef(String notebookName, String noteTitle, Integer delRefNoteId)
    {
        Integer noteId = noteRepository.findByUsernameAndNotebookNameAndNoteTitle(noteService.getUsername(),notebookName,noteTitle).getId();

        NoteDo noteDo = noteRepository.findById(delRefNoteId).get();

        String relativeFileName = notebookName + "/" + noteTitle + ".md";
        String filePath = noteService.getOrCreateUserNotebookDir() + "/" + relativeFileName;
        String stringToRemove  = "["+noteDo.getNoteTitle()+"](&&"+delRefNoteId+")";
        boolean removed = false;

        try {

            // Read the content of the Markdown file
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                // Remove the desired string from the content only once
                if (!removed && line.contains(stringToRemove)) {
                    String updatedLine = line.replaceFirst(Pattern.quote(stringToRemove), "");
                    content.append(updatedLine).append("\n");
                    removed = true; // Mark as removed to skip additional occurrences
                } else {
                    content.append(line).append("\n");
                }
            }
            reader.close();

            // Write the updated content back to the Markdown file
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(content.toString());
            writer.close();

            System.out.println("String removed successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }

//        noteRefRepository.deleteSingleRecord(getUsername(),noteId,delRefNoteId);
        NoteRefDo noteRefDo = noteRefRepository.findTopByUsernameAndNoteIdAndRefNoteId(noteService.getUsername(),noteId,delRefNoteId);
        noteRefRepository.delete(noteRefDo);
        GitUtil.addAndCommit(noteService.getOrCreateUserGit(), relativeFileName);
        noteService.invalidateCache(noteService.buildUserNoteKey(notebookName, noteTitle));
        return ServerResponse.buildSuccessResponse();
    }

    @Override
    public ServerResponse createNoteRef(Integer noteId, Integer noteRefId)
    {
        noteRefRepository.save(new NoteRefDo().setUsername(noteService.getUsername()).setNoteId(noteId).setRefNoteId(noteRefId));
        return ServerResponse.buildSuccessResponse();
    }

    @Override
    public ServerResponse<List<NotebookVo>> getNoteRef(Integer noteId)
    {
        List<NoteRefDo> noteRefDos = noteRefRepository.findAllByUsernameAndNoteId(noteService.getUsername(),noteId);
        List<NotebookVo> notebookVos = new ArrayList<>();

        Map<String, List<NoteVo>> noteBookRefMap = new HashMap<>();
        for(NoteRefDo noteRef : noteRefDos)
        {
            NoteDo noteDo = noteRepository.findById(noteRef.getRefNoteId()).get();
            if(!noteBookRefMap.containsKey(noteDo.getNotebookName()))
                noteBookRefMap.put(noteDo.getNotebookName(),new ArrayList<>());
            noteBookRefMap.get(noteDo.getNotebookName()).add(new NoteVo().setNoteId(noteDo.getId()).setNotebookName(noteDo.getNotebookName()).setTitle(noteDo.getNoteTitle()));
        }

        for (Map.Entry<String, List<NoteVo>> entry : noteBookRefMap.entrySet()) {
            String key = entry.getKey();
            List<NoteVo> value = entry.getValue();
            notebookVos.add(new NotebookVo().setNotebookName(key).setNoteList(value));
        }
        return ServerResponse.buildSuccessResponse(notebookVos);
    }

    @Override
    public RefGraphVo getRefGraph()
    {
        RefGraphVo refGraphVo = new RefGraphVo();

        List<NoteRefDo> noteRefDos = noteRefRepository.findAllByUsername(noteService.getUsername());
        List<NoteDo> noteDos = noteRepository.findAllByUsername(noteService.getUsername());

        List<RefGraphVo.Node> nodes = new ArrayList<>();
        List<RefGraphVo.Line> lines = new ArrayList<>();

        Set<String> ns = new HashSet<>();

        for(NoteRefDo noteRefDo : noteRefDos) {
            lines.add(new RefGraphVo.Line().setFrom(noteRefDo.getNoteId().toString()).setTo(noteRefDo.getRefNoteId().toString()));
            ns.add(noteRefDo.getNoteId().toString());
            ns.add(noteRefDo.getRefNoteId().toString());
        }
        for(NoteDo noteDo : noteDos) {
            if(ns.contains(noteDo.getId().toString()))
                nodes.add(new RefGraphVo.Node().setId(noteDo.getId().toString()).setText(noteDo.getNoteTitle()));
        }


        refGraphVo.setNodes(nodes);
        refGraphVo.setLines(lines);
        return refGraphVo;
    }
}

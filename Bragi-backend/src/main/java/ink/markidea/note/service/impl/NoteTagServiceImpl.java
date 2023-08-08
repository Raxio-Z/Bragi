package ink.markidea.note.service.impl;

import ink.markidea.note.dao.NoteRepository;
import ink.markidea.note.dao.NoteTagRepository;
import ink.markidea.note.entity.NoteDo;
import ink.markidea.note.entity.NoteTagDo;
import ink.markidea.note.entity.dto.NotePreviewInfo;
import ink.markidea.note.entity.resp.ServerResponse;
import ink.markidea.note.entity.vo.NoteVo;
import ink.markidea.note.service.INoteService;
import ink.markidea.note.service.INoteTagService;
import ink.markidea.note.util.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NoteTagServiceImpl implements INoteTagService {

    @Autowired
    private INoteService noteService;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private NoteTagRepository noteTagRepository;

    @Override
    public ServerResponse addNoteTag(String notebookName, String noteName, String tagName) {
        try {
            Integer noteId = noteRepository.findByUsernameAndNotebookNameAndNoteTitle(noteService.getUsername(), notebookName, noteName).getId();
            if (!noteTagRepository.findByNoteIdAndTagName(noteId, tagName).isEmpty()) {
                return ServerResponse.buildErrorResponse("笔记" + noteName + "已经存在标签" + tagName);
            }
            noteTagRepository.save(new NoteTagDo().setNoteId(noteId).setTagName(tagName));
        } catch (Exception e) {
            return ServerResponse.buildErrorResponse(e.getMessage());
        }
        return ServerResponse.buildSuccessResponse();
    }

    @Override
    public ServerResponse deleteNoteTag(String notebookName, String noteName, String tagName) {
        try {
            Integer noteId = noteRepository.findByUsernameAndNotebookNameAndNoteTitle(noteService.getUsername(), notebookName, noteName).getId();
            List<NoteTagDo> tagsToDelete = noteTagRepository.findByNoteIdAndTagName(noteId, tagName);
            if (tagsToDelete.isEmpty()){
                throw new RuntimeException("笔记" + noteName + "不存在标签" + tagName);
            }
            for (NoteTagDo tag : tagsToDelete) {
                noteTagRepository.deleteById(tag.getId());
            }
        } catch (Exception e) {
            return ServerResponse.buildErrorResponse(e.getMessage());
        }
        return ServerResponse.buildSuccessResponse();
    }

    @Override
    public ServerResponse<List<NoteTagDo>> getTagsByNote(String notebookName, String noteName) {
        try {
            Integer noteId = noteRepository.findByUsernameAndNotebookNameAndNoteTitle(noteService.getUsername(), notebookName, noteName).getId();
            List<NoteTagDo> tags = noteTagRepository.findAllByNoteId(noteId);
            return ServerResponse.buildSuccessResponse(tags);
        } catch (Exception e) {
            return ServerResponse.buildErrorResponse(e.getMessage());
        }
    }

    @Override
    public ServerResponse<List<NoteVo>> searchNoteByTag(String tagName) {
        try {
            List<NoteTagDo> noteTags = noteTagRepository.findAllByTagName(tagName);
            List<NoteVo> notes = noteTags.stream().map(NoteTagDo::getNoteId).filter(noteId -> {
                // 这一步filter确保noteId都是可被当前用户访问的有效项
                Optional<NoteDo> noteDoOptional = noteRepository.findById(noteId);
                return noteDoOptional.filter(noteDo -> (Objects.equals(noteService.getUsername(), noteDo.getUsername()))).isPresent();
            }).map(noteId -> {
                Optional<NoteDo> noteDoOptional = noteRepository.findById(noteId);
                if (!noteDoOptional.isPresent())
                    throw new RuntimeException("被索引的笔记" + noteId.toString() + "不存在");
                NoteDo noteDo = noteDoOptional.get();
                String notebookName = noteDo.getNotebookName();
                String noteName = noteDo.getNoteTitle();
                File noteFile = new File(new File(noteService.getOrCreateUserNotebookDir(), notebookName), noteName);

                NoteVo noteVo = new NoteVo().setNotebookName(noteDo.getNotebookName())
                        .setTitle(noteDo.getNoteTitle())
                        .setLastModifiedTime(DateTimeUtil.dateToStr(new Date(noteFile.lastModified())))
                        .setTags(getTagsByNote(notebookName, noteName).getData().stream().map(NoteTagDo::getTagName).collect(Collectors.toList()));
                NotePreviewInfo previewInfo = noteService.getContentPreview(notebookName, noteName);
                if (previewInfo != null) {
                    noteVo.setArticleId(previewInfo.getArticleId())
                            .setPreviewContent(previewInfo.getPreviewContent());
                }
                return noteVo;
            }).collect(Collectors.toList());

            return ServerResponse.buildSuccessResponse(notes);
        } catch (Exception e) {
            return ServerResponse.buildErrorResponse(e.getMessage());
        }
    }

    public ServerResponse<List<String>> getTags() {
        try {
            return ServerResponse.buildSuccessResponse(noteTagRepository.selectDistinctTagsByUsername(noteService.getUsername()));
        } catch (Exception e) {
            return ServerResponse.buildErrorResponse(e.getMessage());
        }
    }
}

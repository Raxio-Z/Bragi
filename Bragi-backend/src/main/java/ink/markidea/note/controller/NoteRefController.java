package ink.markidea.note.controller;


import ink.markidea.note.entity.resp.ServerResponse;
import ink.markidea.note.entity.vo.NotebookVo;
import ink.markidea.note.service.INoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ink.markidea.note.service.INoteRefService;


import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/noteref")
public class NoteRefController {
    @Autowired
    private INoteService noteService;

    @Autowired
    private INoteRefService noteRefService;

    @PutMapping("/refinsert")
    public ServerResponse insertNoteRef(@RequestBody Map<String, Object> params) {
        String noteBookName = params.get("curNoteBookName").toString();
        String noteTitle = params.get("curNoteTitle").toString();
        Integer refNoteId = Integer.valueOf(params.get("refNoteId").toString());

        Integer noteId = noteService.getNoteId(noteBookName, noteTitle).getData();

        noteRefService.createNoteRef(noteId, refNoteId);
        return ServerResponse.buildSuccessResponse();
    }

    @PostMapping("/getnoteref")
    public ServerResponse<List<NotebookVo>> getNoteRef(@RequestBody Map<String, Object> params) {
        String noteBookName = params.get("notebookName").toString();
        String noteTitle = params.get("noteTitle").toString();

        Integer noteId = noteService.getNoteId(noteBookName, noteTitle).getData();

        List<NotebookVo> noteRefDos = noteRefService.getNoteRef(noteId).getData();
        return ServerResponse.buildSuccessResponse(noteRefDos);
    }

    @PostMapping("/delnoteref")
    public ServerResponse deleteNoteRef(@RequestBody Map<String, Object> params) {
        String noteBookName = params.get("notebookName").toString();
        String noteTitle = params.get("noteTitle").toString();
        Integer delRefNoteId = Integer.valueOf(params.get("delRefNoteId").toString());

        noteRefService.deleteNoteRef(noteBookName, noteTitle, delRefNoteId);

        return ServerResponse.buildSuccessResponse();
    }


}

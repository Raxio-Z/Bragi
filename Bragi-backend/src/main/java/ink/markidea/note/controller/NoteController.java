package ink.markidea.note.controller;

import ink.markidea.note.entity.NoteTagDo;
import ink.markidea.note.entity.NoteRefDo;
import ink.markidea.note.entity.req.NoteRequest;
import ink.markidea.note.entity.req.NoteSearchRequest;
import ink.markidea.note.entity.req.NoteTagFetchRequest;
import ink.markidea.note.entity.req.NoteTagModifyRequest;
import ink.markidea.note.entity.resp.ServerResponse;
import ink.markidea.note.entity.vo.NoteVersionVo;
import ink.markidea.note.entity.vo.NoteVo;
import ink.markidea.note.entity.vo.NotebookVo;
import ink.markidea.note.entity.vo.RefGraphVo;
import ink.markidea.note.service.INoteRefService;
import ink.markidea.note.service.INoteService;
import ink.markidea.note.service.INoteTagService;
import org.apache.catalina.Server;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/note")
public class NoteController {

    @Autowired
    private INoteService noteService;

    @Autowired
    private INoteTagService noteTagService;

    @Autowired
    private INoteRefService noteRefService;

    /**
     * save changes of note
     *
     * @param notebookName
     * @param noteTitle
     * @param request
     * @return
     */
    @PostMapping("/{notebookName}/{noteTitle}")
    public ServerResponse saveNote(@PathVariable String notebookName,
                                   @PathVariable String noteTitle,
                                   @RequestBody NoteRequest request) {

        if (StringUtils.isNotBlank(request.getVersionRef())) {
            return noteService.resetAndGet(notebookName, noteTitle, request.getVersionRef());
        }
        if (request.isTmpSave()) {
            noteService.tmpSaveNote(noteTitle, notebookName, request.getContent());
            return ServerResponse.buildSuccessResponse();
        }
        System.out.println(noteTitle);//文件名
        System.out.println(notebookName);//文件夹名
        System.out.println(request.getContent() + "?");//啥也没有

        return noteService.saveNote(noteTitle, notebookName, request.getContent());
    }

    @PostMapping("/upload")
    public ServerResponse uploadNote(HttpServletRequest request) {
        MultipartHttpServletRequest params = ((MultipartHttpServletRequest) request);
        MultipartFile file = ((MultipartHttpServletRequest) request)
                .getFile("file");
        String directory = params.getParameter("directory");

        //上传笔记
        noteService.uploadnote(file, directory);

        return ServerResponse.buildSuccessResponse();
    }


    @PostMapping("/search")
    public ServerResponse<List<NoteVo>> searchNote(@RequestBody NoteSearchRequest request) {
        if (StringUtils.isBlank(request.getKeyWord())) {
            throw new IllegalArgumentException();
        }
        return noteService.search(request.getKeyWord(), request.getSearchNotebookList());

    }


    /**
     * get all notebooks, including notes
     */
    @GetMapping("")
    public ServerResponse<List<NotebookVo>> getNotebooks() {
        List<String> notebookNames = noteService.listNotebooks().getData();
        List<NotebookVo> notebookVoList = new ArrayList<>(notebookNames.size());
        for (String notebookName : notebookNames) {
            NotebookVo notebookVo = new NotebookVo().setNotebookName(notebookName).setNoteList(new ArrayList<>());
            notebookVo.setNoteList(noteService.listNotes(notebookName).getData());
            notebookVoList.add(notebookVo);
        }
        return ServerResponse.buildSuccessResponse(notebookVoList);
    }

    /**
     * get all notes of a notebook
     *
     * @param notebookName
     * @return
     */
    @GetMapping("/{notebookName}")
    public ServerResponse<List<NoteVo>> getNotes(@PathVariable String notebookName) {
        return noteService.listNotes(notebookName);
    }

    @GetMapping("/path/{noteId}")
    public ServerResponse<String> getNotePath(@PathVariable Integer noteId) {
        return noteService.getNotePath(noteId);
    }


    /**
     * get content of note
     */
    @GetMapping("/{notebookName}/{noteTitle}")
    public ServerResponse<String> getNote(@PathVariable String notebookName, @PathVariable String noteTitle) {
        return noteService.getNote(notebookName, noteTitle);
    }


    @GetMapping("/{notebookName}/{noteTitle}/history")
    public ServerResponse<List<NoteVersionVo>> getNoteHistory(@PathVariable String notebookName, @PathVariable String noteTitle) {
        return noteService.getNoteHistory(notebookName, noteTitle);
    }

    /**
     * create or rename a notebook
     */
    @PutMapping("/{notebookName}")
    public ServerResponse createNotebook(@PathVariable String notebookName, @RequestBody NoteRequest request) {
        if (Boolean.TRUE.equals(request.getMove())) {
            noteService.renameNotebook(request.getSrcNotebook(), notebookName);
            return ServerResponse.buildSuccessResponse(noteService.listNotes(notebookName).getData());
        }
        return noteService.createNotebook(notebookName);
    }

    /**
     * copy or move a note to other notebook
     */
    @PutMapping("/{targetNotebook}/{targetNoteTitle}")
    public ServerResponse copyOrMoveNote(@PathVariable String targetNotebook,
                                         @PathVariable String targetNoteTitle,
                                         @RequestBody NoteRequest request) {
        String srcNotebook = request.getSrcNotebook();
        String srcTitle = request.getSrcTitle();
        if (request.getMove() != null && request.getMove()) {
            return noteService.moveNote(srcNotebook, srcTitle, targetNotebook, targetNoteTitle);
        } else {
            return noteService.copyNote(srcNotebook, targetNotebook, targetNoteTitle);
        }
    }


    /**
     * delete a notebook
     *
     * @param notebookName
     * @return
     */
    @DeleteMapping("/{notebookName}")
    public ServerResponse delNotebook(@PathVariable String notebookName) {
        return noteService.deleteNotebook(notebookName);
    }

    /**
     * delete a note
     */
    @DeleteMapping("/{notebookName}/{noteTitle}")
    public ServerResponse delNote(@PathVariable String notebookName,
                                  @PathVariable String noteTitle,
                                  Boolean delDraft) {
        if (Boolean.TRUE.equals(delDraft)) {
            noteService.delTmpSavedNote(noteTitle, notebookName);
            return ServerResponse.buildSuccessResponse();
        }
        return noteService.deleteNote(notebookName, noteTitle);
    }


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


    @GetMapping("/refgraph")
    public ServerResponse<RefGraphVo> getRefGraph() {
        RefGraphVo refGraphVo = noteRefService.getRefGraph();
        return ServerResponse.buildSuccessResponse(refGraphVo);
    }

    /**
     * 添加笔记标签。
     */
    @PostMapping("/tag")
    public ServerResponse addTag(@RequestBody NoteTagModifyRequest request) {
        return noteTagService.addNoteTag(request.getNotebookName(), request.getNoteName(), request.getTagName());
    }

    @GetMapping("/tag")
    public ServerResponse<List<NoteTagDo>> getTagsByNote(@RequestParam String notebookName, @RequestParam String noteName) {
        return noteTagService.getTagsByNote(notebookName, noteName);
    }

    @DeleteMapping("/tag")
    public ServerResponse deleteTag(@RequestParam String notebookName, @RequestParam String noteName, @RequestParam String tagName) {
        return noteTagService.deleteNoteTag(notebookName, noteName, tagName);
    }

    @GetMapping("/tagsearch")
    public ServerResponse<List<NoteVo>> searchNoteByTag(@RequestBody String tagName) {
        return noteTagService.searchNoteByTag(tagName);
    }

    @GetMapping("/tags")
    public ServerResponse<List<String>> getTags() {
        return noteTagService.getTags();
    }

}

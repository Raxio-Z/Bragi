package ink.markidea.note.service;


import ink.markidea.note.entity.NoteTagDo;
import ink.markidea.note.entity.resp.ServerResponse;
import ink.markidea.note.entity.vo.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 *  
 */
public interface INoteService {

    /**
     * list user's all notebooks and their notes
     */
    ServerResponse<List<String>> listNotebooks();

    /**
     * list all notes of the notebook
     */
    ServerResponse<List<NoteVo>> listNotes(String notebookName);


    ServerResponse<List<NoteVo>> search(String keyWord, List<String> searchNotebooks);

    /** create a notebook */
    ServerResponse createNotebook(String notebookName);

    ServerResponse createNote(String noteTitle, String notebookName, String content, List<String> noteTags);


    ServerResponse createNoteRef(Integer noteId, Integer noteRefId);
    /**
     * save changes of note.
     * If the note don't exist, it will be created.
     */
    ServerResponse saveNote(String noteTitle, String notebookName, String content);

    ServerResponse uploadnote(MultipartFile file, String directory);

    /**
     * save note temporarily
     */
    void tmpSaveNote(String noteTitle, String notebookName, String content);

    void delTmpSavedNote(String noteTitle, String notebookName);

    /**
     * delete note
     */
    ServerResponse deleteNote(String notebookName, String noteTitle);


    ServerResponse deleteNoteRef(String notebooName,String noteTitle, Integer delRefNoteId);
    /**
     * get the content of note
     */
    ServerResponse<String> getNote(String notebookName, String noteTitle);

    ServerResponse<String> getNotePath(Integer noteId);

    /**
     * get the content of note
     */
    ServerResponse<String> getNote(String notebookName, String noteTitle, String username);

    /**
     * get the Id of note
     */
    ServerResponse<Integer> getNoteId(String notebookName, String noteTitle);
    /**
     * get version history of note
     */
    ServerResponse<List<NoteVersionVo>> getNoteHistory(String notebookName, String noteTitle);

    /**
     * get note content at a history version
     */
    ServerResponse<String> getNoteHistoryContent(String notebookName, String noteTitle, String versionRef);

    /**
     * recover note to a certain version
     */
    ServerResponse<String> resetAndGet(String notebookName, String noteTitle, String versionRef);

    /**
     * delete notebook and its all notes
     */
    ServerResponse deleteNotebook(String notebookName);

    /**
     * rename a notebook
     */
    void renameNotebook(String srcNotebookName, String targetNotebookName);

    /**
     * list all delnotes
     */
    ServerResponse<List<DeletedNoteVo>> listDelNotes();

    /** recover deleted note.
     * if the corresponding notebook is deleted, it will be created
     */
    ServerResponse recoverNote(Integer id);

    /**
     * clear delnote record
     */
    ServerResponse clearDelNote(Integer id);


    /**
     * move a note
     */
    ServerResponse moveNote(String srcNotebook, String srcTitle, String targetNotebook, String targetTitle);

    /**
     * clear all delnotes by username
     */
    ServerResponse clearAllDelNotes();

    /**
     * copy a note
     * @param srcNotebook
     * @param targetNotebook
     * @param title
     * @return
     */
    ServerResponse copyNote(String srcNotebook, String targetNotebook, String title);

    ServerResponse<List<NotebookVo>> getNoteRef(Integer noteId);

    /**
     * 获取用户文件夹目录
     */
    File getOrInitUserNotebookDir();

    RefGraphVo getRefGraph();

//    void updateNotesNotebookName(String srcNotebook, String targetNotebook);
    ServerResponse addNoteTag(String notebookName, String noteName, String tagName);

    ServerResponse deleteNoteTag(String notebookName, String noteName, String tagName);

    ServerResponse<List<NoteTagDo>> getTagsByNote(String notebookName, String noteName);

    ServerResponse<List<NoteVo>> searchNoteByTag(String tagName);

    ServerResponse<List<String>> getTags();
}

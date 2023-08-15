package ink.markidea.note.service;

import ink.markidea.note.entity.NoteTagDo;
import ink.markidea.note.entity.resp.ServerResponse;
import ink.markidea.note.entity.vo.NoteVo;

import java.util.List;

public interface INoteTagService {

    ServerResponse addNoteTag(String notebookName, String noteName, String tagName);

    ServerResponse deleteNoteTag(String notebookName, String noteName, String tagName);

    ServerResponse<List<NoteTagDo>> getTagsByNote(String notebookName, String noteName);

    ServerResponse<List<NoteVo>> searchNoteByTag(String tagName);

    ServerResponse<List<String>> getTags();
}

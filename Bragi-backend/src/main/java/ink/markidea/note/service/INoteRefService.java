package ink.markidea.note.service;

import ink.markidea.note.entity.resp.ServerResponse;
import ink.markidea.note.entity.vo.NotebookVo;
import ink.markidea.note.entity.vo.RefGraphVo;

import java.util.List;

public interface INoteRefService {

    ServerResponse createNoteRef(Integer noteId, Integer noteRefId);

    ServerResponse deleteNoteRef(String notebooName,String noteTitle, Integer delRefNoteId);

    ServerResponse<List<NotebookVo>> getNoteRef(Integer noteId);

    RefGraphVo getRefGraph();
}

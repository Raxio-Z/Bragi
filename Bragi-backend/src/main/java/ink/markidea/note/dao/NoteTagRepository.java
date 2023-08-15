package ink.markidea.note.dao;

import ink.markidea.note.entity.NoteTagDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface NoteTagRepository extends JpaRepository<NoteTagDo, Integer> {
    // POST /api/note/tag: save

    // GET /api/note/tag
    List<NoteTagDo> findAllByNoteId(Integer noteId);

    // DELETE /api/note/tag:
    @Transactional
    void deleteAllByNoteIdAndTagName(Integer noteId, String tagName);

    // GET /api/note/tagsearch
    List<NoteTagDo> findAllByTagName(String tagName);

    // GET /api/note/tags
    @Query("select distinct t.tagName from NoteTagDo t")
    List<String> selectDistinctGlobalTags();

    @Query("select distinct ttag.tagName from NoteDo tnote inner join NoteTagDo ttag on tnote.id = ttag.noteId where tnote.username = :userName")
    List<String> selectDistinctTagsByUsername(String userName);

    // auxiliaries
    List<NoteTagDo> findByNoteIdAndTagName(Integer noteId, String tagName);

    void deleteAllByNoteId(Integer noteId);
}

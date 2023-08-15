package ink.markidea.note.dao;

import ink.markidea.note.entity.NoteRefDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface NoteRefRepository extends JpaRepository<NoteRefDo, Integer> {
    List<NoteRefDo> findAllByUsernameAndNoteId(String username, Integer noteId);

    List<NoteRefDo> findAllByUsername(String username);

    NoteRefDo findTopByUsernameAndNoteIdAndRefNoteId(String username, Integer noteId, Integer refNoteId);

    @Transactional
    void deleteByUsernameAndNoteIdAndRefNoteId(String username, Integer noteId, Integer refNoteId);

    @Transactional
    void deleteById(Integer id);

}

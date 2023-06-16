package ink.markidea.note.dao;

import ink.markidea.note.entity.NoteDo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<NoteDo,Integer> {
//    @Transactional
//    void deleteByUsernameAndNotebookNameAndNoteTitle(String username, String notebookName, String noteTitle);

    NoteDo findByUsernameAndNotebookNameAndNoteTitle(String username, String notebookName, String noteTitle);

    @Transactional
    void deleteByUsernameAndNotebookNameAndNoteTitle(String username, String notebookName, String noteTitle);

    @Transactional
    @Modifying
    @Query("update NoteDo n set n.notebookName = :targetNotebook where n.notebookName = :srcNotebook and n.username = :username")
    int updateNotebookName(@Param("username") String username, @Param("srcNotebook") String srcNotebook, @Param("targetNotebook") String targetNotebook);

    @Query("select distinct n.notebookName from NoteDo n where n.username = :username")
    List<String> findNoteBookNameByUsername(@Param("username") String username);

    List<NoteDo> findAllByUsernameAndNotebookName(String username, String notebookName);

    List<NoteDo> findAllByUsername(String username);
}

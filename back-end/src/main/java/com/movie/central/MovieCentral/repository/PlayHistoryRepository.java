package com.movie.central.MovieCentral.repository;

import com.movie.central.MovieCentral.model.Movie;
import com.movie.central.MovieCentral.model.PlayHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PlayHistoryRepository extends JpaRepository<PlayHistory, Long> {

    @Query(value = "select * from play_history p, movie m where p.customer_id = ?1 and p.movie_id = m.id order by p.play_time DESC ", nativeQuery = true)
    List<PlayHistory> findMovieAndPlayHistoryByCustomer_Id(Long customerId);

    @Query(value = "select * from play_history p, movie m, customer c where p.customer_id = ?1 and p.movie_id = ?2 and p.movie_id = m.id and p.customer_id = c.id", nativeQuery = true)
    List<PlayHistory> findPlayHistoryByCustomerIdAndMovieId(Long customerId, Long movieId);

    @Query(value = "select m.id, m.title, count(p.movie_id) as playcount from movie m  left join play_history p on m.id = p.movie_id group by m.id , m.title order by playcount desc", nativeQuery = true)
    List<Object[]> getPlayPerMovie();

    @Query(value = "select p.movie_id, m.title, count(p.movie_id) as playcount from play_history p, movie m where p.movie_id = m.id group by p.movie_id order by playcount desc limit 10", nativeQuery = true)
    List<Object[]> getMostPlayedMovies();

    @Query(value = "select p.movie_id, m.title, count(p.movie_id) as playcount from play_history p, movie m where p.movie_id = m.id and p.play_time between ?1 and ?2 group by p.movie_id order by playcount desc limit 10", nativeQuery = true)
    List<Object[]> getMostPlayedMoviesByPlayTime(LocalDateTime startDateTime, LocalDateTime endDateTime);

    @Query(value = "select p.customer_id, m.name, count(p.customer_id) as playcount from play_history p, customer m where p.customer_id = m.id and p.play_time between ?1 and ?2 group by customer_id order by playcount desc limit 10", nativeQuery = true)
    List<Object[]> getMostActiveCustomersByPlayTime(LocalDateTime startDateTime, LocalDateTime endDateTime);

    @Query(value = "select count(distinct p.customer_id) as playcount from play_history p, customer c where p.customer_id = c.id and p.play_time between ?1 and ?2", nativeQuery = true)
    Long getActiveCustomersByPlayTime(LocalDateTime startDateTime, LocalDateTime endDateTime);

    @Query(value = "select distinct m.id, m.title, COUNT(p.id) as playcount, m.average_rating, m.image_url\n" +
            "from  play_history p  join movie m on  p.movie_id = m.id  and p.play_time between ?1 AND ?2 and m.status = 'ACTIVE'\n" +
            "group by m.id, m.title\n" +
            "order by playcount desc\n" +
            "limit 10", nativeQuery = true)
    List<Object[]> getTopTenMoviesPlayCountInMonth(LocalDateTime startDateTime, LocalDateTime endDateTime);



}

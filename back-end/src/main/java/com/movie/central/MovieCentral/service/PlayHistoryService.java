package com.movie.central.MovieCentral.service;


import com.movie.central.MovieCentral.exceptions.Error;
import com.movie.central.MovieCentral.exceptions.MovieCentralException;
import com.movie.central.MovieCentral.model.Customer;
import com.movie.central.MovieCentral.model.Movie;
import com.movie.central.MovieCentral.model.PlayHistory;
import com.movie.central.MovieCentral.repository.CustomerRepository;
import com.movie.central.MovieCentral.repository.MovieRepository;
import com.movie.central.MovieCentral.repository.PlayHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlayHistoryService {

    @Autowired
    PlayHistoryRepository playHistoryRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    MovieRepository movieRepository;


    public void addPLayRecord(Long customerId, Long movieId) throws Exception{
        Optional<Customer> customer = customerRepository.findById(customerId);
        Movie movie = movieRepository.findMovieById(movieId);
        if(customer.isPresent() && movie != null){
            List<PlayHistory> playHistory = playHistoryRepository.findPlayHistoryByCustomerIdAndMovieId(customerId, movieId);
            playHistory = playHistory.stream().filter(history -> history.getPlayTime().isAfter(LocalDateTime.now(ZoneId.systemDefault()).minusDays(1))).collect(Collectors.toList());
            if(playHistory.size()<=0){
                PlayHistory playHistory1 = new PlayHistory();
                playHistory1.setCustomer(customer.get());
                playHistory1.setMovie(movie);
                LocalDateTime playTime = LocalDateTime.now(ZoneId.systemDefault());
                playHistory1.setPlayTime(playTime);

                playHistoryRepository.save(playHistory1);
            }
        }

    }
}

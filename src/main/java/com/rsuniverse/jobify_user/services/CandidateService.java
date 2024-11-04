package com.rsuniverse.jobify_user.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.rsuniverse.jobify_user.models.dtos.CandidateDTO;
import com.rsuniverse.jobify_user.models.entities.Candidate;
import com.rsuniverse.jobify_user.models.responses.PaginatedRes;
import com.rsuniverse.jobify_user.repos.CandidateRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CandidateService {

    private final CandidateRepo jobSeekerRepo;
    private final ModelMapper modelMapper;

    public PaginatedRes<CandidateDTO> getAllCandidates(int page, int size) {
        log.info("Fetching all jobSeekers - page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page - 1, size); // 1 index to 0 index
        Page<Candidate> jobSeekerPage = jobSeekerRepo.findAll(pageable);

        List<CandidateDTO> jobSeekers = jobSeekerPage.getContent().stream()
                .map(jobSeeker -> modelMapper.map(jobSeeker, CandidateDTO.class))
                .collect(Collectors.toList());

        return PaginatedRes.<CandidateDTO>builder()
                .items(jobSeekers)
                .pageNum(jobSeekerPage.getNumber() + 1) // 0 index to 1 index
                .pageSize(jobSeekerPage.getSize())
                .totalPages(jobSeekerPage.getTotalPages())
                .totalCount(jobSeekerPage.getTotalElements())
                .build();
    }

    public CandidateDTO getCandidateById(String id) {
        log.info("Fetching jobSeeker by id: {}", id);
        Candidate jobSeeker = jobSeekerRepo.findById(id).orElseThrow(()
                -> new RuntimeException("Candidate not found with id: " + id));
        return modelMapper.map(jobSeeker, CandidateDTO.class);
    }

    public CandidateDTO createCandidate(CandidateDTO jobSeekerDTO) {
        log.info("Creating jobSeeker: {}", jobSeekerDTO);
        Candidate jobSeeker = modelMapper.map(jobSeekerDTO, Candidate.class);
        jobSeekerRepo.save(jobSeeker);
        log.info("Candidate created successfully: {}", jobSeeker);
        return modelMapper.map(jobSeeker, CandidateDTO.class);
    }

    public CandidateDTO updateCandidate(String id, CandidateDTO jobSeekerDTO) {
        log.info("Updating jobSeeker with id: {}", id);
        Candidate jobSeeker = jobSeekerRepo.findById(id).orElseThrow(()
                -> new RuntimeException("Candidate not found with id: " + id));
        modelMapper.map(jobSeekerDTO, jobSeeker); 
        jobSeekerRepo.save(jobSeeker);
        log.info("Candidate updated successfully: {}", jobSeeker);
        return modelMapper.map(jobSeeker, CandidateDTO.class);
    }

    public void deleteCandidate(String id) {
        log.info("Deleting jobSeeker with id: {}", id);
        if (jobSeekerRepo.existsById(id)) {
            jobSeekerRepo.deleteById(id);
            log.info("Candidate with id {} deleted successfully", id);
        } else {
            log.warn("Candidate with id {} not found", id);
        }
    }
}

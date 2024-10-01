package com.example.demo.service;

import com.example.demo.entity.KoiFish;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.KoiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service // danh dau day la mot lop xu ly logic
public class KoiFishService {
    // xu ly nhung logic lien qua
    @Autowired
    KoiRepository koiRepository;
    public KoiFish createNewKoi(KoiFish koi){
        //add fish vao database bang repsitory
        try {
            KoiFish newKoi = koiRepository.save(koi);
            return newKoi;
        }catch (Exception  e){
            throw new DuplicateEntity("Duplicate Koi id !");
        }

    }
    public List<KoiFish> getAllKoi(){
        // lay tat ca student trong DB
        List<KoiFish> kois = koiRepository.findKoiByIsDeletedFalse();
        return kois;
    }
    public KoiFish updateKoiFish(KoiFish koi, long id){
        // buoc 1: tim toi thang student co id nhu la FE cung cap
        KoiFish oldKoi = koiRepository.findKoiById(id);
        if(oldKoi ==null){
            throw new NotFoundException("Koi not found !");//dung viec xu ly ngay tu day
        }
        //=> co farm co ton tai
        oldKoi.setFarm(koi.getFarm());
        oldKoi.setBreed(koi.getBreed());
        oldKoi.setName(koi.getName());
        return koiRepository.save(oldKoi);
    }
    public KoiFish deleteKoi(long id){
        KoiFish oldKoi = koiRepository.findKoiById(id);
        if(oldKoi ==null){
            throw new NotFoundException("Koi not found !");//dung viec xu ly ngay tu day
        }
        oldKoi.setDeleted(true);
        return koiRepository.save(oldKoi);
    }
}

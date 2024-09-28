package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.Admin;
import com.example.demo.entity.Sale;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.AdminRequest;
import com.example.demo.repository.AdminRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    // xu ly nhung logic lien qua
    @Autowired
    AdminRepository adminRepository;
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    ModelMapper modelMapper;
    public Admin createNewAdmin(AdminRequest adminRequest){
        // add student vào database bằng repository
        try {
            Admin admins = modelMapper.map(adminRequest,Admin.class);
            // xac dinh account nao tai ai student nao
            // thong qua duoc filter
            // filter luu lai duoc account yeu cau tao student nay r
            Account accountRequest=authenticationService.getCurrentAccount();
            admins.setAccount(accountRequest);
            Admin newAdmin = adminRepository.save(admins);
            return newAdmin; // trả về 1 thằng mới
        } catch (Exception e) {
            throw new DuplicateEntity("Duplicate admin code!");
        }
    }
    public List<Admin> getAllAdmin(){
        // lay tat ca student trong DB
        List<Admin> admins = adminRepository.findAccountsByIsDeletedFalse();
        return admins;
    }
    public Admin updateAdmin(Admin admin, long id){

        Admin oldAdmin = adminRepository.findAccountById(id);
        if(oldAdmin ==null){
            throw new NotFoundException("Admin not found !");//dung viec xu ly ngay tu day
        }
        //=> co farm co ton tai;
        oldAdmin.setPhone(admin.getPhone());
        oldAdmin.setEmail(admin.getEmail());
        oldAdmin.setFullName(admin.getFullName());
        return adminRepository.save(oldAdmin);
    }
    public Admin deleteAdmin(long id){
        Admin oldAdmin = adminRepository.findAccountById(id);
        if(oldAdmin ==null){
            throw new NotFoundException("Admin not found !");//dung viec xu ly ngay tu day
        }
        oldAdmin.setDeleted(true);
        return adminRepository.save(oldAdmin);
    }
}

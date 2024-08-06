package com.sparta.nexusteam.employee.service;

import com.sparta.nexusteam.employee.dto.DepartmentResponse;
import com.sparta.nexusteam.employee.entity.Department;
import com.sparta.nexusteam.employee.entity.Employee;
import com.sparta.nexusteam.employee.entity.UserRole;
import com.sparta.nexusteam.employee.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    /*@Cacheable(value = "departments", key = "#employee.company.id")*/
    public List<DepartmentResponse> getAllDepartments(Employee employee) {
        List<Department> departmentList = departmentRepository.findAllByCompany(employee.getCompany());
        List<DepartmentResponse> departmentResponseList = new ArrayList<>();
        for(Department department : departmentList) {
            DepartmentResponse response = new DepartmentResponse(department.getId(), department.getName());
            departmentResponseList.add(response);
        }
        return departmentResponseList;
    }

    @Override
    /*@Caching(
            put = @CachePut(value = "department", key = "#result.id"),
            evict = @CacheEvict(value = "departments", key = "#employee.company.id")
    )*/
    public Department createDepartment(String departmentName, Employee employee) {
        if (departmentRepository.existsByNameAndCompany(departmentName, employee.getCompany())) {
            throw new IllegalArgumentException("이미 존재하는 이름입니다");
        }

        if(!UserRole.MANAGER.equals(employee.getRole())){
            throw new AccessDeniedException("권한이 없습니다");
        }

        Department department = new Department();
        department.setName(departmentName);
        department.setCompany(employee.getCompany());

        departmentRepository.save(department);

        return department;
    }

    @Override
    /*@Caching(
            put = @CachePut(value = "department", key = "#result.id"),
            evict = @CacheEvict(value = "departments", key = "#employee.company.id")
    )*/
    public Department updateDepartment(Long id, Department departmentDetails, Employee employee) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("부서를 찾을 수 없습니다 " + id));

        if (!department.getName().equals(departmentDetails.getName()) &&
                departmentRepository.existsByNameAndCompany(departmentDetails.getName(), employee.getCompany())) {
            throw new IllegalArgumentException("이미 존재하는 이름입니다");
        }

        if(!UserRole.MANAGER.equals(employee.getRole())){
            throw new AccessDeniedException("권한이 없습니다");
        }

        department.updateName(departmentDetails.getName());

        departmentRepository.save(department);

        return department;
    }

    @Override
    /*@Caching(
            evict = {
                    @CacheEvict(value = "department", key = "#id"),
                    @CacheEvict(value = "departments", key = "#employee.company.id")
            }
    )*/
    public Long deleteDepartment(Long id, Employee employee) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("부서를 찾을 수 없습니다 " + id));

        if(!UserRole.MANAGER.equals(employee.getRole())){
            throw new AccessDeniedException("권한이 없습니다");
        }

        departmentRepository.delete(department);

        return department.getId();
    }
}

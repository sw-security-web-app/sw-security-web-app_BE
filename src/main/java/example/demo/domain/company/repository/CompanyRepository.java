package example.demo.domain.company.repository;

import example.demo.domain.company.Company;
import example.demo.domain.company.repository.CompanyCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company,Long> , CompanyCustomRepository {
    Optional<Company> findByCompanyNameAndCompanyDept(String companyName,String companyDept);
}

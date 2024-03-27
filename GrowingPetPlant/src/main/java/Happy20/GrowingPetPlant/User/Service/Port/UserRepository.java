package Happy20.GrowingPetPlant.User.Service.Port;

import Happy20.GrowingPetPlant.User.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsById(String id);
    User findById(String id); // 아이디로 사용자 조회
}
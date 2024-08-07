package Happy20.GrowingPetPlant.User.Controller;

import Happy20.GrowingPetPlant.JWT.TokenDto;
import Happy20.GrowingPetPlant.User.DTO.*;
import Happy20.GrowingPetPlant.User.Service.Port.UserRepository;
import Happy20.GrowingPetPlant.User.Service.UserService;
import Happy20.GrowingPetPlant.User.Domain.User;
import Happy20.GrowingPetPlant.UserPlant.Service.Port.UserPlantRepository;
import Happy20.GrowingPetPlant.UserPlant.Service.UserPlantService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final UserPlantService userPlantService;

    // 회원가입 api
    @PostMapping("/sign-up")
    public ResponseEntity<String> signup(@Valid @RequestBody PostSignupReq postSignupReq) {

        if (userRepository.existsById(postSignupReq.getId())) // 아이디 중복 체크
            return ResponseEntity.status(HttpStatus.OK).body("중복된 아이디입니다.\n");

        if (userRepository.existsByUserName(postSignupReq.getUserName())) // 닉네임 중복 체크
            return ResponseEntity.status(HttpStatus.OK).body("중복된 닉네임입니다.\n");

        if (userRepository.existsByPhoneNumber(postSignupReq.getPhoneNumber())) // 핸드폰 번호 중복 체크
            return ResponseEntity.status(HttpStatus.OK).body("중복된 핸드폰 번호입니다.\n");

        User newUser = userService.createUser(postSignupReq); // 유저 정보 생성

        userPlantService.createUserPlant(newUser, postSignupReq.getUserPlantName(), postSignupReq.getPlantType()); // 유저-식물 및 상태, 그래프 정보 생성

        return ResponseEntity.status(HttpStatus.OK).body("회원가입에 성공했습니다.\n");
    }

    // 로그인 api
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody PostLoginReq postLoginReq, HttpServletResponse response) {
        TokenDto userToken = userService.validationLogin(postLoginReq, response);
        if (userToken != null) {
            return ResponseEntity.status(HttpStatus.OK).body(userToken);
        } else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

//    // 회원가입 - 아이디 중복 검사 api
//    @GetMapping("/idCheck")
//    public ResponseEntity<String> idCheck(@RequestParam("id") String id) {
//        if (userService.validateId(id))
//            return ResponseEntity.status(HttpStatus.OK).body("사용 가능한 아이디입니다.");
//        else
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용할 수 없는 아이디입니다.");
//    }
//
//    // 회원가입 - 닉네임 중복 검사 api
//    @GetMapping("/nameCheck")
//    public ResponseEntity<String> nameCheck(@RequestParam("name") String name) {
//        if (userService.validateName(name))
//            return ResponseEntity.status(HttpStatus.OK).body("사용 가능한 닉네임입니다.");
//        else
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 존재하는 닉네임입니다.");
//    }

    // 마이페이지 수정 api
    @PatchMapping("/mypage")
    public ResponseEntity<String> updateMyPage(@Valid @RequestBody PatchUpdateMyPageReq patchUpdateMyPageReq) {
        if (userService.validateUpdateMyPage(patchUpdateMyPageReq)) {
            return ResponseEntity.status(HttpStatus.OK).body("마이페이지가 수정됐습니다.");
        } else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("마이페이지를 수정할 수 없습니다.");
    }

    // 유저 번호 -> 유저 찾기 api
    @GetMapping("/findUser")
    public ResponseEntity<User> findUser(@RequestParam("userNumber") Long userNumber) {
        User user = userService.validateUser(userNumber);

        if (user == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        else
            return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    // 아이디 찾기 api
    @PostMapping("/findId")
    public ResponseEntity<String> findId(@RequestBody GetFindUserIdReq getFindUserIdReq) {
        String userId = userService.matchUserId(getFindUserIdReq);
        if (userId != null)
            return ResponseEntity.status(HttpStatus.OK).body(getFindUserIdReq.getUserName() +
                    " 사용자의 아이디는\n[" + userId + "] 입니다.");
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("존재하지 않는 사용자입니다.");
    }

    // 비밀번호 찾기 api
    @PostMapping("/findPwd")
    public ResponseEntity<String> findPwd(@RequestBody GetFindUserPwdReq getFindUserPwdReq) {
        String userPwd = userService.matchUserPwd(getFindUserPwdReq);
        if (userPwd != null)
            return ResponseEntity.status(HttpStatus.OK).body(getFindUserPwdReq.getUserName() +
                    "사용자의 비밀번호는\n [" + userPwd + "] 입니다.");
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("존재하지 않는 사용자입니다.");
    }

    // 로그아웃 api
    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request){
        //세션 무효화
        request.getSession().invalidate();

        //클라이언트에게 로그아웃 성공 메시지와 HTTP 상태 코드 반환
        return new ResponseEntity<>("로그아웃 되었습니다", HttpStatus.OK);
    }

    // 회원 탈퇴 api
    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable String id){
        userService.deleteUser(id);
        return id+ " 사용자 탈퇴가 완료되었습니다.";
    }
}
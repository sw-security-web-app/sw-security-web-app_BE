//package example.demo.domain.chat.controller;
//
//import example.demo.domain.chat.AIModelType;
//import example.demo.domain.chat.Chat;
//import example.demo.domain.chat.dto.request.ChatDto;
//import example.demo.domain.chat.repository.ChatRepository;
//import example.demo.domain.member.Member;
//import example.demo.domain.member.MemberStatus;
//import example.demo.domain.member.dto.request.MemberRequestDto;
//import example.demo.domain.member.repository.MemberRepository;
//import example.demo.security.auth.dto.CustomMemberInfoDto;
//import example.demo.security.util.JwtUtil;
//import jakarta.persistence.EntityManager;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@Transactional
//class ChatAIControllerTest {
//
//    @Autowired private MockMvc mockMvc;
//    @Autowired private ChatRepository chatRepository;
//    @Autowired private MemberRepository memberRepository;
//    @Autowired private JwtUtil jwtUtil;
//    @Autowired private EntityManager em;
//    private Long testMemberId;
//
//    @BeforeEach
//    public void 초기설정() {
//        Member member1 = Member.createGeneral(
//                MemberRequestDto.ofGeneral("timer973@naver.com"
//                        ,"박재성"
//                        ,"wotjd1025@"
//                        ,"010-6636-1710"
//                        , "GENERAL")
//        );
//        memberRepository.save(member1);
//        testMemberId = member1.getMemberId();
//
//        Chat chat1 = Chat.builder()
//                .modelType(AIModelType.Gemini)
//                .question("안녕")
//                .answer("안녕")
//                .member(member1)
//                .build();
//
//        Chat chat2 = Chat.builder()
//                .modelType(AIModelType.Gemini)
//                .question("잘가")
//                .answer("잘가")
//                .member(member1)
//                .build();
//
//        chatRepository.save(chat1);
//        chatRepository.save(chat2);
//
//        em.flush();
//    }
//
//    @Test
//    public void 최신_대화내용_조회() throws Exception {
//        // given
//        CustomMemberInfoDto memberInfoDto = new CustomMemberInfoDto(
//                1L,
//                "timer973@naver.com",
//                "wotjd1025@",
//                MemberStatus.GENERAL,
//                false
//        );
//        String token = jwtUtil.createAccessToken(memberInfoDto);
//        // when
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/chat-ai/latest")
//                        .header("Authorization", "Bearer" + token)
//                        .contentType("application/json"))
//                .andExpect(status().isOk());
//
//        // then
//        Page<ChatDto> chatList = chatRepository.findLatestChatListByMember(testMemberId, PageRequest.of(0, 4));
//        assertThat(chatList).isNotEmpty();
//        assertThat(chatList.getTotalElements()).isEqualTo(2);
//        assertThat(chatList.getContent()).hasSize(2);
//        assertThat(chatList.getContent().get(0).getQuestion()).isEqualTo("안녕");
//        assertThat(chatList.getContent().get(0).getAnswer()).isEqualTo("안녕");
//        assertThat(chatList.getContent().get(1).getQuestion()).isEqualTo("잘가");
//        assertThat(chatList.getContent().get(1).getAnswer()).isEqualTo("잘가");
//    }
//}
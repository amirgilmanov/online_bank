package com.example.online_bank.service;

import com.example.online_bank.domain.dto.QuestResponseDto;
import com.example.online_bank.domain.dto.UserQuestWithProgress;
import com.example.online_bank.domain.entity.Quest;
import com.example.online_bank.domain.entity.User;
import com.example.online_bank.domain.entity.UserCategoryStats;
import com.example.online_bank.domain.entity.UserQuest;
import com.example.online_bank.enums.PartnerCategory;
import com.example.online_bank.mapper.QuestMapper;
import com.example.online_bank.repository.QuestRepository;
import com.example.online_bank.repository.UserQuestRepository;
import com.example.online_bank.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class QuestService {
    private final QuestRepository questRepository;
    private final Random random = new Random();
    private final UserRepository userRepository;
    private final UserQuestRepository userQuestRepository;
    private final QuestMapper questMapper;
    private final UserCategoryStatsService userCategoryStatsService;
    private final UserService userService;

    public Quest create(PartnerCategory category) {
        int randomPoint = generateRandomPoint();
        LocalDate expDate = createExpDate();
        Quest quest = Quest.builder()
                .pointReward(randomPoint)
                .category(category)
                .dateOfExpiry(expDate)
                .progress(generateProgress())
                .build();
        questRepository.save(quest);
        List<User> verifiedUsers = userRepository.findAllIsVerified();

        List<UserQuest> userQuests = verifiedUsers.stream()
                .map(user ->
                        UserQuest.builder()
                                .isComplete(false)
                                .user(user)
                                .userProgress(0)
                                .quest(quest)
                                .build()
                )
                .toList();
        userQuestRepository.saveAll(userQuests);
        return quest;
    }

    @Scheduled(cron = "0 0 0 1 * *")
    public List<QuestResponseDto> createRandomQuest() {
        PartnerCategory[] categories = PartnerCategory.values();
        Set<PartnerCategory> randomCategories = Stream
                .generate(() -> random.nextInt(0, PartnerCategory.values().length))
                .limit(3)
                .map(e -> categories[e])
                .collect(Collectors.toSet());

        return randomCategories.stream()
                .map(this::create)
                .map(questMapper::toDto)
                .toList();
    }

    private int generateRandomPoint() {
        return random.nextInt(1, 11) * 50;
    }

    private int generateProgress() {
        return random.nextInt(2, 6);
    }

    public List<UserQuestWithProgress> findAllByUserQuest(UUID userUuid) {
        User user = userService.findByUuid(userUuid).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
        UserCategoryStats userStats = userCategoryStatsService.findByUserUuid(userUuid).orElseGet(
                () -> UserCategoryStats
                        .builder()
                        .user(user)
                        .totalSpend(BigDecimal.ZERO)
                        .spendPeriod(null)
                        .countSpendInMonth(0)
                        .category(null)
                        .build()
        );
        //если не нашел прогресса пользователя - то выдай, что прогресс на всех квестах, которые ему доступны равны 0
        //все записи квестами - пользователей
        List<UserQuest> allUserQuests = userQuestRepository.findAllByUser_Uuid(userUuid);
        return allUserQuests.stream().map(
                userQuest -> {
                    Quest quest = userQuest.getQuest();
                    return createUserProgress(userQuest, quest, userStats.getCountSpendInMonth());
                }
        ).toList();
    }

    private UserQuestWithProgress createUserProgress(UserQuest userQuest, Quest quest, Integer countSpendInMonth) {
        return new UserQuestWithProgress(
                generateQuestName(quest),
                quest.getCategory(),
                quest.getDateOfExpiry(),
                quest.getPointReward(),
                quest.getProgress(),
                countSpendInMonth,
                userQuest.getIsComplete()
        );
    }

    private List<Quest> findAllByDateOfExpiryIsAfter(LocalDate date) {
        return questRepository.findAllByDateOfExpiryIsAfter(date);
    }

    private String generateQuestName(Quest quest) {
        return "Квест № %s".formatted(quest.getId());
    }

    public List<Quest> findAllAvalaible(LocalDate now) {
        //find all Quest where now before now.последнийДень
        return questRepository.findAllByDateOfExpiryIsAfter(now);
    }

    private LocalDate createExpDate() {
        YearMonth currentYearMonth = YearMonth.now();
        int lastDayOfMonth = currentYearMonth.lengthOfMonth();
        return LocalDate.of(currentYearMonth.getYear(), currentYearMonth.getMonth(), lastDayOfMonth);
    }
}

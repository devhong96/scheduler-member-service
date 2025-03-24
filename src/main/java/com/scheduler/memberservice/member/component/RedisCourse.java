package com.scheduler.memberservice.member.component;

import com.scheduler.memberservice.member.student.domain.Student;
import com.scheduler.memberservice.member.student.repository.StudentJpaRepository;
import com.scheduler.memberservice.member.teacher.domain.Teacher;
import com.scheduler.memberservice.member.teacher.repository.TeacherJpaRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisCourse {

    @Value("${spring.data.redis.redis-preload-enabled:true}") // 기본값 true
    private boolean redisPreloadEnabled;

    private final TeacherJpaRepository teacherJpaRepository;
    private final StudentJpaRepository studentJpaRepository;
    private final RedissonClient redissonClient;

    private static final String TEACHER_CACHE_NAME = "teacher";
    private static final String STUDENT_CACHE_NAME = "student";

    private static final long CACHE_TTL = 30;
    private final RedisTemplate<String, Object> redisTemplate;

    @PostConstruct
    @Scheduled(cron = "30 59 23 * * SUN", zone = "Asia/Seoul")
    public void preloadAllDataToRedis() {

        if (!redisPreloadEnabled) {
            log.info("🔹 테스트 환경이므로 실행하지 않음");
            return;
        }

        RLock lock = redissonClient.getLock("redis_course_lock");

        if (!lock.tryLock()) {
            log.info("이미 다른 인스턴스에서 실행 중이므로 종료.");
            return;
        }

        try {
            log.info("🔄 전체 데이터 Redis에 미리 캐싱");
            redisTemplate.delete(redisTemplate.keys(TEACHER_CACHE_NAME + "*"));
            redisTemplate.delete(redisTemplate.keys(STUDENT_CACHE_NAME + "*"));

            List<Teacher> teacherList = teacherJpaRepository.findAll();
            if (teacherList.isEmpty()) {
                log.info("⚠ 교사 데이터 없음. 캐싱하지 않음.");
                return;
            }

            List<Student> studentList = studentJpaRepository.findAll();
            if (studentList.isEmpty()) {
                log.info("⚠ 학생 데이터 없음. 캐싱하지 않음.");
                return;
            }

            redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
                for (Student student : studentList) {
                    String cacheKey = generateStudentCacheKey(student);
                    redisTemplate.opsForValue().set(cacheKey, student, CACHE_TTL, TimeUnit.SECONDS);
                }
                return null;
            });

            redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
                for (Teacher teacher : teacherList) {
                    String cacheKey = generateTeacherCacheKey(teacher);
                    redisTemplate.opsForValue().set(cacheKey, teacher, CACHE_TTL, TimeUnit.SECONDS);
                }
                return null;
            });

        } finally {
            lock.unlock(); // 락 해제
        }
    }

    private String generateTeacherCacheKey(Teacher teacher) {
        return TEACHER_CACHE_NAME + ":username:" + teacher.getTeacherId();
    }

    private String generateStudentCacheKey(Student student) {
        return STUDENT_CACHE_NAME + ":username:" + student.getStudentId();
    }
}

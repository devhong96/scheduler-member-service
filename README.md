# 📌Scheduler-Member-Service

---
Scheduler-Member-Service는 학생을 관리하는 서비스

이 서비스는 다양한 사용자(학생, 교사)들이 자신의 수업 일정을 효율적으로 확인하고 관리할 수 있도록 API를 제공합니다.

---
## 🛠 API

🔗 [Swagger UI](https://seho0218.synology.me:8087/swagger-ui/index.html?urls.primaryName=scheduler-member-service)

---

## 🛠 설계 및 구조

멀티 컨테이너 환경에서 서비스되며 API Gateway에서 1차적으로 토큰을 이용해 보안성을 검증하고 각 서비스간통신은 openfeign을 통해 이루어 집니다.

<br>

학생 1만명, 교사 2001명으로 이루어져있습니다.



<br>





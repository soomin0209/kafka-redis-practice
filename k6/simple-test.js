import http from 'k6/http';

export const options = {
    vus: 5,
    duration: '10s',
};

export default function () {
    http.get("http://host.docker.internal:8080/actuator/health");
}

// localhost는 "요청을 보내는 주체"를 의미
// → 현재 k6는 Docker 컨테이너에서 실행 중이므로 localhost = k6 컨테이너 내부
// → 컨테이너 내부에는 Spring 서버가 없기 때문에 요청 실패

// host.docker.internal은 Docker 컨테이너에서 호스트 PC를 가리키는 주소
// → k6 컨테이너 → host.docker.internal → 호스트 PC → Spring 서버(8080)

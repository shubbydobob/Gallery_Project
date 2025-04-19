import axios from "axios";
import {useAccountStore} from "@/stores/account.js";

const instance = axios.create();

// 인터센터 (응답 시)
instance.interceptors.response.use((response) => {
    return response;
}, async (error) => {
    switch (error.response.status) {
        case 400:
            // 잘못된 요청
            window.alert("Something went wrong!");
            break;

        case 401:
            // 권한 없음
            const config = error.config;
            if (config.retried) {
                window.alert("권한이 없습니다.");
                window.location.replace("/");
                return;
            }
            // 쿠키에 있는 리프레시 토큰으로 액세스 토큰 요청
            const res = await axios.get("/v1/api/account/token");

            const accessToken = res.data;

            if (!accessToken) {
                // 새 토큰이 빈 문자열이면 로그인 실패 처리
                window.alert("자동 로그인 실패. 다시 로그인 해주세요.");
                window.location.replace("/login");
                return Promise.reject(); // 요청 중단
            }


            const accountStore = useAccountStore();

            // 계정 스토어의 엑세스 토큰 변경
            accountStore.setAccessToken(accessToken);

            // 요청 엑세스 토큰 교체
            config.headers.authorization = `Bearer ${accountStore.accessToken}`;

            // 중복 재요청 방지를 위한 프로퍼티 추가
            config.retried = true;
jj
            // 재요청
            return instance(config);

        // 오류 발생
        case 500:
            window.alert("알 수 없는 오류가 발생했습니다.");
            break;
    }
    return Promise.reject(error);
});

// HTTP 요청 설정 생성
const generateConfig = () => {
    const accountStore = useAccountStore();

    if (accountStore.accessToken) {
        return {
            headers: {authorization: `Bearer ${accountStore.accessToken}`},
        };
    }
    return {};
};

export default {
    get(url, params) {
        const config = generateConfig();
        config.params = params;
        return instance.get(url, config);
    },
    post(url, params) {
        return instance.post(url, params, generateConfig());
    },
    put(url, params) {
        return instance.put(url, params, generateConfig());
    },
    delete(url) {
        return instance.delete(url, generateConfig());
    },
};
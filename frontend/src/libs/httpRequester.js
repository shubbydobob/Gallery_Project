import axios from "axios";

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
            window.alert("권한이 없습니다.");
            window.location.replace("/");
            break;

            // 오류 발생
        case 500:
            window.alert("알 수 없는 오류가 발생했습니다.");
            break;
    }
    return Promise.reject(error);
});

export default {
    get: (url, params) => {
        return instance.get(url, { params });
    },
    post: (url, data) => {
        return instance.post(url, data);
    },
    put: (url, data) => {
        return instance.put(url, data);
    },
    delete: (url) => {
        return instance.delete(url);
    },
};
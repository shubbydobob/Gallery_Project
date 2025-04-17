import axios from "axios";

// 장바구니 상품 목록 조회
export const getItems = () => {
    return axios.get("/vi/api/cart/items").catch(e => e.response);
}

// 장바구니 상품 추가
export const addItem = (itemId) => {
    return axios.post("/vi/api/carts", { itemId }).catch(e => e.response);
}

// 장바구니에서 상품 삭제
export const removeItem = (itemId) => {
    return axios.delete(`/vi/api/cart/items/${itemId}`).catch(e => e.response);
}
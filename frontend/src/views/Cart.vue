<script setup>
import {getItems, removetem} from "@/services/cartService.js";
import {reactive} from "vue";

// 반응형 상태
const state = reactive({
  items: []
});

// 장바구니 상품 조회
const load = async () => {
  const res = await getItems();

  if(res.status === 200) {
    state.items = res.data;
  }
}

// 장바구니 상품 삭제
const remove = async (itemId) => {
  const res = await removeItem(itemId);

  if(res.status === 200) {
    window.alert("선택하신 장바구니의 상품을 삭제했습니다.");
    await load();
  }
}

// 커스텀 생성 훅
(async  function onCreated() {
  await  load();
})();
</script>


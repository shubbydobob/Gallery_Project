<script setup>
import {useRouter} from "vue-router";
import {useAccountStore} from "@/stores/account";

const router = useRouter();
const accountStore = useAccountStore();

// 상품 등록 전 로그인 체크, 로그인 페이질 이동 유도
const loginCheck = async () => {
  if (!accountStore.loggedIn) {
    if (window.confirm("로그인이 필요합니다. 로그인 페이지로 이동하시겠습니까?")) {
      await router.push("/login");
    }
    return;
  }
};
</script>

<template>
  <div class="research">
    <div class="container">
      <button @click="loginCheck()">상품 등록</button>
    </div>
  </div>
</template>

<style scoped lang="scss">

</style>
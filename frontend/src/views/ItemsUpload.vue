<script setup>
import {useRouter} from "vue-router";
import {reactive} from "vue";
</script>

<template>
  <div class="items-upload">
    <h1>상품 등록</h1>
    <form @submit.prevent="submit">
      <input type="file" @change="onFileChange" />
      <button type="submit">등록하기</button>
    </form>
  </div>
</template>

<style scoped lang="scss">

</style>
<script setup>
import {reactive} from "vue";
import {getOrders} from "@/services/orderService.js";

// 반응형 상태
const state = reactive({
  args: {
    page: 0,
    size: 5,
  },
  page: {
    index: 0,
    totalPages: 0,
    totalElements: 0,
  },
  orders: []
});

const getListNum = (idx) => {
  return state.page.totalElements - idx - state.args.size * state.page.index;
};

// 주문 목록 조회
const load = async (pageIdx) => {
  if (pageIdx !== undefined) {
    state.args.page = pageIdx;
  }

  const res = await getOrders(state.args);

  if (res.status === 200) {
    state.orders = res.data.content;
    state.page.index = res.data.number;
    state.page.totalPages = res.data.totalPages;
    state.page.totalElements = res.data.totalElements;
  }
};

// 커스텀 생성 훅
(async function onCreated() {
  await load();
})();
</script>

<template>
  <div class="orders">
    <div class="container">
      <table class="table table-bordered my-4">
        <thead>
        <tr>
          <th class="text-center">번호</th>
          <th>주문자명</th>
          <th>결제 수단</th>
          <th>결제 금액</th>
          <th>결제일시</th>
          <th>자세히 보기</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(o, idx) in state.orders">
          <td class="text-center">{{ getListNum(idx) }}</td>
          <td>{{ o.name }}</td>
          <td>{{ o.payment === 'card' ? '카드' : '무통장입금' }}</td>
          <td>{{ o.amount.toLocaleString() }}원</td>
          <td>{{ o.created.toLocaleString() }}</td>
          <td>
            <router-link :to="`/orders/${o.id}`">자세히 보기</router-link>
          </td>
        </tr>
        </tbody>
      </table>
      <div class="pagination d-flex justify-content-center pt-2">
        <div class="btn-group" role="group">
          <button class="btn py-2 px-3"
                  :class="[state.page.index === idx ? 'btn-primary' : 'btn-outline-primary']"
                  v-for="(i, idx) in state.page.totalPages" @click="load(idx)">
            {{ i }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
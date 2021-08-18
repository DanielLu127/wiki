<template>
  <a-layout>
    <a-layout-sider width="200" style="background: #fff">
    <a-menu
        mode="inline"
        v-model:selectedKeys="selectedKeys2"
        v-model:openKeys="openKeys"
        :style="{ height: '100%', borderRight: 0 }"
    >
      <a-sub-menu>
        <template #title>
          <span>
            All
          </span>
        </template>
      </a-sub-menu>
      <a-sub-menu v-for="item in categoryTree" :key="item.id">
        <template #title>
          <span>
            {{item.name}}
          </span>
        </template>
        <a-menu-item v-for="childrenItem in item.children" :key="childrenItem.id">
           <span>
            {{childrenItem.name}}
          </span>
        </a-menu-item>
      </a-sub-menu>
    </a-menu>
  </a-layout-sider>
    <a-layout-content
      :style="{ background: 'rgb(255,255,255)', padding: '24px', margin: 0, minHeight: '280px' }"
  >
      <a-list item-layout="vertical" size="large" :grid="{ gutter: 20, column: 3 }" :data-source="ebooks">
        <template #renderItem="{ item }">   <!--/这里会遍历循环ebooks，将每个元素赋值给item-->
          <a-list-item key="item.name">
            <template #actions>
              <span v-for="{ type, text } in actions" :key="type">
                <component v-bind:is="type" style="margin-right: 8px" />
                {{ text }}
              </span>
            </template>

            <a-list-item-meta :description="item.description">
              <template #title>
                <a :href="item.href">{{ item.name }}</a>
              </template>
              <template #avatar><a-avatar :src="item.cover" /></template>
            </a-list-item-meta>
          </a-list-item>
        </template>
      </a-list>

  </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref, reactive, toRef} from 'vue';
import axios from 'axios';
import {Tool} from "../../util/tool";
import {message} from "ant-design-vue";

const listData: any = [];

export default defineComponent({
  name: 'Home',

  setup() {
    const categoryTree = ref();
    const handleCategoryQuery = () => {
      axios.get("/category/all").then((response) => {
        //后端返回的结果
        const data = response.data;
        if (data.success) {
          categoryTree.value = Tool.array2Tree(data.content, 0);
        } else {
          message.error(data.message);
        }
      });
    };
    const ebooks = ref() //用ref可以让变量变成响应式数据，只有响应式数据可以实时刷新到界面上
    //const ebooks1 = reactive({books: []}); reactive是另外一种让变量变成响应式数据的方法
    //生命周期函数onMounted, setup函数执行的时候界面还没有渲染好
    //所以尽量把初始化内容写进生命周期函数
    onMounted( () => {
      handleCategoryQuery();
      //在main.ts里配置了baseURL所以URL不用写全
      axios.get("/ebook/list").then((response) => {
        const data = response.data;
        ebooks.value = data.content.list;
      });
    });
    //将变量返回给html
    return {
      categoryTree,
      ebooks,
      pagination:  {
        onChange: (page: any) => {
          console.log(page);
        },
        pageSize: 3,
      },
      actions: [
        { type: 'StarOutlined', text: '156' },
        { type: 'LikeOutlined', text: '156' },
        { type: 'MessageOutlined', text: '2' },
      ],
    }
  }
});
</script>

<!--scoped: 表示这里的样式只在当前的组件起作用-->
<!-- 这里重新定义ant-avator样式-->
<style scoped>
  .ant-avatar {
    width:50px;
    height: 50px;
    line-height: 50px;
    border-radius: 8%;
    margin: 5px 0;
  }
</style>

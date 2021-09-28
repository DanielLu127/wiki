<template>
  <a-layout>
    <a-layout-sider width="230"
        :style="{ overflow: 'auto', height: '100vh', position: 'fixed', left: 0, 'padding-top':'60px'}"
    >
      <a-menu
          mode="inline"
          :style="{ height: '100%', borderRight: 0 }"
          @click="handleClick"
      >
        <a-menu-item key="All">
           <span>
            All
          </span>
        </a-menu-item>
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
    <a-layout>
      <a-layout-content :style="{ background: 'rgb(255,255,255)', 'padding-left': '250px',minHeight: '28px', 'padding-top': '90px'}">
      <a-form layout="inline" :model="search" :style="{'padding-left': '20px', position: 'fixed', zIndex: 1}">
        <a-form-item :style="{width: '300px'}">
          <a-input v-model:value="search.name" placeholder="Name">
          </a-input>
        </a-form-item>
        <a-button @click="queryWithoutCategory()" size='medium'>
          Search
        </a-button>
      </a-form>
      <a-list item-layout="vertical" size="large"
                :grid="{ gutter: 20, column: 1 }"
                :data-source="ebooks"
                :style="{'padding-top': '60px'}">
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
              </a-list-item-meta>
            </a-list-item>
          </template>
        </a-list>
    </a-layout-content>
    </a-layout>
  </a-layout>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref, reactive, toRef} from 'vue';
import axios from 'axios';
import {Tool} from "../../util/tool";
import {message} from "ant-design-vue";

export default defineComponent({
  name: 'Home',

  setup() {
    const categoryTree = ref();
    let categoryId2 = 0;
    const search = ref();
    search.value = {}; //必须要初始化
    const ebooks = ref(); //用ref可以让变量变成响应式数据，只有响应式数据可以实时刷新到界面上
    //const ebooks1 = reactive({books: []}); reactive是另外一种让变量变成响应式数据的方法
    const queryWithCategory = () => {
      //在main.ts里配置了baseURL所以URL不用写全
      axios.get("/ebook/list", {
        params: {
          categoryId2: categoryId2
        }
      }).then((response) => {
        const data = response.data;
        ebooks.value = data.content.list;
      });
    }
    const queryWithoutCategory = () => {
      axios.get("/ebook/list", {
        params: {
          name: search.value.name,
        }
      }).then((response) => {
        //后端返回的结果
        const data = response.data;
        if (data.success) {
          ebooks.value = data.content.list;
        } else {
          message.error(data.message);
        }
      })
    }
    const handleClick = (value: any) => {
      if (value.key === 'All') {
        axios.get("/ebook/list").then((response) => {
          const data = response.data;
          ebooks.value = data.content.list;
        });
      } else {
        categoryId2 = value.key
        queryWithCategory();
      }
    }
    const queryCategory = () => {
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

    onMounted( () => {
      queryCategory();
      queryWithoutCategory();
    });

    //将变量返回给html
    return {
      categoryTree,
      ebooks,
      handleClick,
      queryWithoutCategory,
      search,
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

<!--: 表示这里的样式只在当前的组件起作用-->
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

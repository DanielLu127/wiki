<template>
  <a-layout>
    <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">
      <p>
        <a-form layout="inline" :model="search">
         <a-form-item>
           <a-input v-model:value="search.name" placeholder="Name">
           </a-input>
         </a-form-item>
          <a-button type="dashed" @click="handleQuery({page: 1, size: pagination.pageSize})" size='medium'>
            Search
          </a-button>
         <a-button type="dashed" @click="add()" size='medium'>
            Add
         </a-button>
       </a-form>
      </p>

      <a-table
          :columns="columns"
          :row-key="record => record.id"
          :data-source="ebooks"
          :pagination="pagination"
          :loading="loading"
          @change="handleTableChange"
      >
        <template #cover="{ text: cover }">
          <img v-if="cover" :src="cover" alt="avatar" />
        </template>
        <template v-slot:action="{ text, record }">
          <a-space size="small">
            <a-button type="dashed" @click="edit(record)">
              Edit
            </a-button>
            <a-popconfirm
                title="Are you sure you want to delete this item?"
                ok-text="Yes"
                cancel-text="No"
                @confirm="handleDelete(record.id)"
            >
              <a-button type="dashed">
                Delete
              </a-button>
            </a-popconfirm>

          </a-space>
        </template>
        <template v-slot:category="{ text, record }">
          <span> {{getCategoryName(text.category1Id)}} / {{getCategoryName(text.category2Id)}}</span>
        </template>
      </a-table>
    </a-layout-content>
  </a-layout>

  <a-modal title="Add a new book"
      v-model:visible="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModalOk"
  >
    <a-form :model="ebook" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="封面">
        <a-input v-model:value="ebook.cover" />
      </a-form-item>
      <a-form-item label="名称">
        <a-input v-model:value="ebook.name" />
      </a-form-item>
      <a-form-item label="分类">
        <a-cascader
          v-model:value="categoryIds"
          :field-names="{ label: 'name', value: 'id', children: 'children' }"
          :options="categoryTree"
        />
      </a-form-item>
      <a-form-item label="描述">
        <a-input v-model:value="ebook.description" type="textarea" />
      </a-form-item>
    </a-form>
  </a-modal>

</template>

<script lang="ts">
import {createVNode, defineComponent, onMounted, ref, reactive, UnwrapRef } from 'vue';
import axios from 'axios';
import {ExclamationCircleOutlined} from "@ant-design/icons-vue";
import { Modal } from 'ant-design-vue';
import { message } from 'ant-design-vue';
import {Tool} from "../../../util/tool";

export default defineComponent({
  name: 'AdminEbook',
  setup() {
    //初始化变量,用于接收后端的数据, 其中ebooks, pagination, loading为响应式变量
    const search = ref();
    search.value = {};
    const ebooks = ref();
    const pagination = ref({
      current: 1,
      pageSize: 3,
      total: 0
    });
    const loading = ref(false);
    const columns = [
      {
        title: '封面',
        dataIndex: 'cover',
        slots: { customRender: 'cover' }
      },
      {
        title: '名称',
        dataIndex: 'name'
      },
      {
        title: '分类',
        slots: { customRender: 'category'}
      },
      {
        title: '文档数',
        dataIndex: 'docCount'
      },
      {
        title: '阅读数',
        dataIndex: 'viewCount'
      },
      {
        title: '点赞数',
        dataIndex: 'voteCount'
      },
      {
        title: 'Action',
        key: 'action',
        slots: { customRender: 'action' }
      }
    ];

    /**
     * 数据查询,调用后端接口
     **/
    const handleQuery = (params: any) => {
      loading.value = true;
      console.log(params);
      axios.get("/ebook/list", {
        params: {
          name: search.value.name,
          page: params.page,
          size: params.size,
        }
      }).then((response) => {
        loading.value = false;
        //后端返回的结果
        const data = response.data;
        if (data.success) {
          ebooks.value = data.content.list;
          categoryIds.value = [ebook.value.category1Id, ebook.value.category2Id]
          // 重置分页按钮
          pagination.value.current = params.page;
          pagination.value.total = data.content.total;
        } else {
          message.error(data.message);
        }

      });
    };

    /**
     * 表格点击页码时触发数据查询函数
     */
    const handleTableChange = (pagination: any) => {
      console.log("看看自带的分页参数都有啥：" + pagination);
      handleQuery({
        page: pagination.current,
        size: pagination.pageSize
      });
    };

    /**
    Click响应函数
    */
    const confirmMessage = () => {
      Modal.confirm({
        title: 'Are you sure delete this task?',
        icon: createVNode(ExclamationCircleOutlined),
        content: 'Some descriptions',
        okText: 'Yes',
        okType: 'danger',
        cancelText: 'No',
        onOk() {
          console.log('OK');
        },
        onCancel() {
          console.log('Cancel');
        },
      });
    };

    /**
     *  -------- 表单 ---------
     */
    const categoryIds = ref(); //数组 [x,y]对应两级分类
    let categorys: any;
    const ebook = ref({
      id: "",
      name: "",
      category1Id:"",
      category2Id:"",
      description: ""
    });
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const handleModalOk = () => {
      if (ebook.value.name == "") {
        message.error("name cannot be null");
        return;
      }

      modalLoading.value = true;
      ebook.value.category1Id = categoryIds.value[0];
      ebook.value.category2Id = categoryIds.value[1];
      axios.post("/ebook/save", ebook.value).then((response) => {
        const data = response.data; //data = commomResp
        modalLoading.value = false;
          if (data.success) {
            modalVisible.value = false;
            //重新加载列表
            handleQuery({
              page: pagination.value.current,
              size: pagination.value.pageSize
            });
          } else {
            message.error(data.message);
          }
      });
    };

    /**
     * edit
     */
    const edit = (record: any) => {
      modalVisible.value = true;
      ebook.value = Tool.copy(record);
      categoryIds.value = [ebook.value.category1Id, ebook.value.category2Id];
    };

    const categoryTree = ref();
    const handleQueryCategory = () => {
      axios.get("/category/all").then((response) => {
        loading.value = false;
        //后端返回的结果
        const data = response.data;
        categorys = data.content;
        if (data.success) {
          categoryTree.value = Tool.array2Tree(data.content, 0);
          //加载完分类后,再加载电子书，否则如果分类树加载很慢， 则电子书渲染会报错
          handleQuery({
            page: 1,
            size: pagination.value.pageSize
          });
        } else {
          message.error(data.message);
        }

      });
    };
    /**
     * Add
     */
    const add = (record: any) => {
      modalVisible.value = true;
      ebook.value = {
        id: "",
        name: "",
        category1Id:"",
        category2Id:"",
        description: ""
      };
    };

    /**
     * Delete
     */
    const handleDelete = (id: number) => {
      axios.delete("/ebook/delete/" + id).then((response) => {
        const data = response.data; //data = commomResp
        if (data.success) {
          //重新加载列表
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize
          });
        }
      });
    };

    const getCategoryName = (cid: number) => {
      let result = "";
      categorys.forEach((item: any) => {
        if (item.id === cid) {
          result = item.name;
        }
      })
      return result;
    }

    /**
     * 初始化显示的数据,传递到后端的参数为page和size,注意名称要和后端对应
     * handleQuery初始时会传给handleQuery函数然后传给后端
     */
    onMounted(() => {
      handleQueryCategory();
    });

    /**
     * 将数据返回给html
    */
    return {
      ebooks,
      pagination,
      columns,
      loading,
      handleTableChange,
      confirmMessage,
      handleQuery,

      edit,
      add,
      handleDelete,
      search,

      ebook,
      modalVisible,
      modalLoading,
      handleModalOk,

      categoryIds,
      categoryTree,
      getCategoryName
    }
  }
});
</script>

<style scoped>
img {
  width: 50px;
  height: 50px;
}
</style>
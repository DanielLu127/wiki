<template>
  <a-layout>
    <a-layout-content :style="{ background: '#fff', padding: '90px', margin: 0, minHeight: '280px' }">
      <p>
         <a-button type="dashed" @click="add()" size='medium'>
            Add
         </a-button>
      </p>
      <!--前面加冒号表示变量的意思-->
      <a-table
          :columns="columns"
          :row-key="record => record.id"
          :data-source="categoryTree"
          :pagination="false"
          :loading="loading"
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
      </a-table>
    </a-layout-content>
  </a-layout>

  <a-modal title="Add a Category"
      v-model:visible="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModalOk"
  >
    <a-form :model="category" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="名称">
        <a-input v-model:value="category.name" />
      </a-form-item>
      <a-form-item label="父分类">
        <a-select
            ref="select"
            v-model:value=category.parent
        >
          <a-select-option value="0">
            None
          </a-select-option>
          <a-select-option v-for="c in categoryTree" :key="c.id" :value="c.id" :disabled="category.id === c.id">
            {{ c.name }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="顺序">
        <a-input v-model:value="category.sort" />
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
  name: 'AdminCategory',
  setup() {
    //初始化变量,用于接收后端的数据, 其中categorys, pagination, loading为响应式变量
    const loading = ref(false);
    const columns = [
      {
        title: '名称',
        dataIndex: 'name'
      },
      {
        title: '父分类',
        key: 'parent',
        dataIndex: 'parent'
      },
      {
        title: '顺序',
        dataIndex: 'sort'
      },
      {
        title: 'Action',
        key: 'action',
        slots: { customRender: 'action' }
      }
    ];
    /**
     * myTree结构
     * [{
     *   id: "",
     *   name: "".
     *   children: [{
     *      id: "",
     *      name: "",
     *      childre: ...
     *   }]
     * }]
     **/
    const categoryTree = ref();

    /**
     * 数据查询,调用后端接口
     **/
    const handleQuery = () => {
      loading.value = true;
      axios.get("/category/all").then((response) => {
        loading.value = false;
        //后端返回的结果
        const data = response.data;
        if (data.success) {
          categoryTree.value = Tool.array2Tree(data.content, 0);
        } else {
          message.error(data.message);
        }

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
    const category = ref();
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const handleModalOk = () => {
      if (category.value.name == "") {
        message.error("name cannot be null");
        return;
      }

      modalLoading.value = true;
      axios.post("/category/save", category.value).then((response) => {
        const data = response.data; //data = commomResp
        modalLoading.value = false;
          if (data.success) {
            modalVisible.value = false;
            //重新加载列表
            handleQuery();
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
      category.value = Tool.copy(record);
    };

    /**
     * Add
     */
    const add = (record: any) => {
      modalVisible.value = true;
      category.value = {
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
      axios.delete("/category/delete/" + id).then((response) => {
        const data = response.data; //data = commomResp
        if (data.success) {
          //重新加载列表
          handleQuery();
        }
      });
    };

    /**
     * 初始化显示的数据,传递到后端的参数为page和size,注意名称要和后端对应
     * handleQuery初始时会传给handleQuery函数然后传给后端
     */
    onMounted(() => {
      handleQuery();
    });

    /**
     * 将数据返回给html
    */
    return {
      categoryTree,
      columns,
      loading,
      confirmMessage,
      handleQuery,

      edit,
      add,
      handleDelete,

      category,
      modalVisible,
      modalLoading,
      handleModalOk
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
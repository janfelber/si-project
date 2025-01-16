<template>
  <transition name="fade">
    <v-alert
        v-if="alert_show"
        class="alerts"
        :color="alert_color"
        variant="elevated"
        :icon="alert_icon"
        :text="alert_text"
    ></v-alert>
  </transition>

  <div>
    <div v-if="loading">Loading...</div>

    <div class="tables-container">

      <div class="form-table">
        <div class="category-cards-container">
          <div v-for="(categoryData, categoryId) in groupedColumns" :key="categoryId" class="category-card">
            <div class="category-header">
              <h2>{{ categoryData.name }}</h2>
            </div>
            <div class="category-content">
              <div v-for="column in categoryData.columns" :key="column.id" class="form-row">
                <label class="form-label" :for="column.id">{{ column.column_name }}</label>
                <div class="form-input">
                  <template v-if="column.type === 'dropdown'"
                            class="form-input">
                    <select
                        :id="column.id"
                        v-model="selectedChoices[column.id]"
                    >
                      <option
                          v-for="choice in column.choices"
                          :key="choice.id"
                          :value="choice.id"
                      >
                        {{ choice.choice_name }}
                      </option>
                    </select>
                  </template>
                  <template v-else>
                    <textarea v-if="column.type === 'text'"
                              :id="column.id"
                              type="text"
                              v-model="selectedChoices[column.id]"
                              class="text-input">

                    </textarea>
                  </template>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="article-info-card">
          <div class="card">
            <h1>Informácie o práci</h1>
            <div class="card-content">
              <p><strong>Názov práce:</strong> {{ article_name }}</p>
              <v-divider></v-divider>
              <p><strong>Klúčové slová:</strong> {{ article_keywords }}</p>
              <v-divider></v-divider>
              <p><strong>Popis práce:</strong> {{ article_description }}</p>
              <v-divider></v-divider>
              <p><strong>Spoluatori:</strong> {{ article_coauthors }}</p>
              <v-divider></v-divider>
              <p><strong>Kategória:</strong> {{ article_section }}</p>
            </div>

          </div>
        </div>

      </div>

    </div>
    <div class="button-container">
      <button class="btn-submit" @click="submitReview()" :disabled="successSend">Akceptovať článok</button>
      <button class="btn-reject" @click="rejectReview()" :disabled="successSend === true">Zamietnuť článok</button>
    </div>
  </div>
</template>

<script>
import router from '@/router/index.js';
import axios from 'axios';

export default {
  data() {
    return {
      columns: [],
      selectedChoices: {},
      loading: true,
      article_id: this.$route.params.id,
      alert_show: false,
      alert_text: "",
      alert_icon: "",
      alert_color: "",
      article_name: "",
      article_keywords: "",
      article_description: "",
      article_coauthors: "",
      article_section: "",
      successSend: false,
    };
  },
  mounted() {
    this.fetchColumns();
    this.getArticleById();
    console.log(this.article_id)
    console.log(this.successSend)

  },
  computed: {
    groupedColumns() {
      const grouped = {};
      this.columns.forEach((column) => {
        if (!grouped[column.category_id]) {
          grouped[column.category_id] = {
            name: column.category_name,
            columns: []
          };
        }
        grouped[column.category_id].columns.push(column);
      });
      return grouped;
    }
  },
  methods: {
    async getArticleById(){
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get(
            `http://localhost:8080/api/v1/article/student/${this.article_id}`,
            {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
        );
        console.log(response.data);
        this.article_name = response.data.articleName;
        this.article_keywords = response.data.keyWords;
        this.article_description = response.data.articleDescription;
        this.article_coauthors = response.data.coAuthors;
        this.article_section = response.data.section;
      } catch (error) {
        console.error('Error fetching article:', error);
      }
    },
    async fetchColumns() {
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get(
            `http://localhost:8080/api/v1/columns/getcolumns`,
            {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
        );
        console.log(response.data);  // Debugging line to see the actual structure of the response
        this.columns = response.data;

      } catch (error) {
        console.error('Error fetching columns:', error);
      } finally {
        this.loading = false;  // Set loading to false when data is fetched
      }
    },
    async submitReview() {
      try {

        this.successSend = true;
        console.log("ako som stlacil submit" + this.successSend);
        const allFieldsFilled = Object.values(this.selectedChoices).every((value) => value !== null && value !== '');
        if (!allFieldsFilled) {
          await this.showAlert("error", "Všetky polia musia byť vyplnené")
          return;
        }

        const token = localStorage.getItem("token");
        const article_id = this.article_id

        const columnValues = {};
        const textValues = {};

        for (let columnId in this.selectedChoices) {
          const value = this.selectedChoices[columnId];

          if (typeof value === 'string') {
            textValues[columnId] = value;
          } else {
            columnValues[columnId] = value;
          }
        }

        const reviewRequest = {
          article_id,
          columnValues,
          textValues,
        };

        console.log(reviewRequest)
        console.log(this.selectedChoices)
        const response = await axios.post(
            `http://localhost:8080/api/v1/review/createReview`,
            reviewRequest,
            {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
        );

        if(response.status === 200){
          await this.showAlert("success", "Článok bol Akceptovaný")
          console.log("pred nastavenim" + this.successSend);
          this.successSend = true;
          console.log("po nastaveni" + this.successSend);
          await router.push({ name: 'reviewConferences' });
        }
        console.log('Review submitted successfully:', response.data);
      } catch (error) {
        await this.showAlert("error", "Článok sa nepodarilo akceptovať")
        console.error('Error submitting review:', error);
      }
    },
    async rejectReview() {
      try {
        const token = localStorage.getItem("token");
        const article_id = this.article_id

        const columnValues = {};
        const textValues = {};

        // through all columns
        for (let columnId in this.selectedChoices) {
          const value = this.selectedChoices[columnId];

          // Check if the value is a number or text and assign it correctly
          if (typeof value === 'string') {
            textValues[columnId] = value;
          } else {
            columnValues[columnId] = value;
          }
        }

        const reviewRequest = {
          article_id,
          columnValues,
          textValues
        };

        const response = await axios.post(
            `http://localhost:8080/api/v1/review/rejectReview`,
            reviewRequest,
            {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
        );
        if (response.status === 200){
          this.successSend = true;
          await this.showAlert("success", "Článok bol zamietnutý")
          console.log("pred nastavenim" + this.successSend);

          console.log("po nastaveni" + this.successSend);
          await router.push({ name: 'reviewConferences' });
        }
        console.log(response)
        console.log('Review submitted successfully:', response.data);
      } catch (error) {
        await this.showAlert("error", "Článok sa nepodarilo zamietnuť")
        console.error('Error submitting review:', error);
      }
    },
    async showAlert(status, message){
      if(status === "success"){
        this.alert_show = true;
        this.alert_text = message;
        this.alert_icon = "$success";
        this.alert_color = "success";
        await new Promise(resolve => setTimeout(resolve, 2000));
        this.alert_show = false;
        this.successSend = false;
      }
      else if (status === "error"){
        this.alert_show = true;
        this.alert_text = message;
        this.alert_icon = "$error";
        this.alert_color = "error";
        await new Promise(resolve => setTimeout(resolve, 2000));
        this.alert_show = false;
      }
    },
  }
};
</script>
<style scoped>

.article-info-card .card {
  background-color: white;
  border: none;
  margin-top: 10px;

}

.article-info-card .card-header h2 {
  margin: 0;
  font-size: 20px;
}

.article-info-card .card-content p {
  margin: 10px 0;
  color: #333;
  font-size: 16px;
}

.article-info-card {
  max-height: fit-content;
  width: fit-content;
  min-width: 20%;
  border:     1px solid #d8d8f0;
  padding: 20px;
  border-radius: .5rem;
  background-color: #ffffff;
  margin-bottom: 10px;
  margin-left: 10px;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.5s;
}
.fade-enter,
.fade-leave-to {
  opacity: 0;
}

.alerts{
  position: fixed;
  top: 20px;
  right: 20px;
  height: fit-content;
  width: fit-content;
  font-size: large;
  z-index: 9999;
}

.tables-container {
  margin-top: 10px;
  display: flex;
  gap: 30px;
}

.form-table {
  flex: 1;
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.article-section {
  max-height: 300px;
  flex: 1;
  padding: 10px 10px 10px 20px;
  border:     1px solid #d8d8f0;
  border-radius: .5rem;
  background-color: #ffffff;
  max-width: 500px;
  margin-left: 10px;
  margin-right: 10px;
}

.category-cards-container {
  gap: 20px;
  flex-wrap: wrap;
}

.category-card {
  max-height: fit-content;
  border:     1px solid #d8d8f0;
  padding: 20px;
  border-radius: .5rem;
  background-color: #ffffff;
  margin-bottom: 10px;
  margin-left: 20px;
}

.category-header {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 15px;
}

.form-row {
  display: flex;
  flex-direction: column;
  margin-bottom: 15px;
}

.form-label {
  font-size: 16px;
  font-weight: 500;
}

.form-input {
  //width: 100%;
}

.form-input input {
  border:     1px solid #d8d8f0;
  padding:    8px;
  width:      100%;
  box-sizing: border-box;
}

.form-input select {
  border:     1px solid #d8d8f0;
  padding:    8px;
  width:      100%;
  box-sizing: border-box;
  appearance: none;
  background: url("data:image/svg+xml,%3Csvg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 16 16\"%3E%3Cpath d=\"M4.293 5.293a1 1 0 0 1 1.414 0L8 7.586l2.293-2.293a1 1 0 0 1 1.414 1.414L8 10.414l-3.707-3.707a1 1 0 0 1 0-1.414z\"%3E%3C/path%3E%3C/svg%3E") no-repeat right 0.75rem center;
  background-size: 8px 8px;
  cursor: pointer;
}

.text-input {
  width: 100%;
  max-width: 300px;
  padding: 10px;
  font-size: 14px;
  border:     1px solid #d8d8f0;
  border-radius: 1px;
}

.btn-submit,
.btn-reject {
  display: inline-block;
  margin: 20px 10px 0;
  padding: 10px 20px;
  font-size: 16px;
  border-radius: 5px;
  border: none;
  cursor: pointer;
}

.btn-submit {
  background-color: green;
  color: #fff;
}

.btn-reject {
  background-color: #dc3545;
  color: #fff;
}

.btn-submit:hover,
.btn-reject:hover {
  opacity: 0.9;
}

.button-container {
  justify-content: center;
  margin-bottom: 20px;
  margin-left: 10px;
  width: 100%;
}

</style>
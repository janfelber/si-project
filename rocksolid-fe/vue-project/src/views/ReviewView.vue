<template>
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
        <div class="article-section">
          <h1>Article</h1>
          <p>Article content goes here...</p>
        </div>
      </div>

    </div>
    <div class="button-container">
      <button class="btn-submit" @click="submitReview()">Submit Review</button>
      <button class="btn-reject" @click="rejectReview()">Reject Article</button>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      columns: [],
      selectedChoices: {},
      loading: true,
      article_id: this.$route.params.id,
    };
  },
  mounted() {
    this.fetchColumns();
    console.log(this.article_id)
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
    // Axios to fetch columns data
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
        const token = localStorage.getItem("token");
        const article_id = this.article_id

        const reviewRequest = {
          article_id,
          columnValues: this.selectedChoices,
        };

        // Send review data to backend
        const response = await axios.post(
            `http://localhost:8080/api/v1/review/createReview`,
            reviewRequest,
            {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
        );
        console.log(article_id)
        console.log('Review submitted successfully:', response.data);
      } catch (error) {
        console.error('Error submitting review:', error);
      }
    },
    async rejectReview() {
      try {
        const token = localStorage.getItem("token");
        const article_id = this.article_id

        const reviewRequest = {
          article_id,
          columnValues: this.selectedChoices,
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
        console.log(article_id)
        console.log('Review submitted successfully:', response.data);
      } catch (error) {
        console.error('Error submitting review:', error);
      }
    },
  }
};
</script>
<style scoped>
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
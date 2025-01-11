<template>
  <div>
<!--    <h1>Grading</h1>-->
    <div v-if="loading">Loading...</div>




    <div class="tables-container">
      <div class="article-section">
        <h1>Article</h1>

        <p>Article content goes here...</p>
      </div>

      <div class="form-table">
        <div v-for="column in columns" :key="column.id" class="form-row">
          <label class="form-label" :for="column.id">{{ column.column_name }}</label>
          <div class="form-input">
            <template v-if="column.type === 'dropdown'">
              <select
                  :id="column.id"
                  v-model="selectedChoices[column.id]"
                  class="dropdown"
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
              <input
                  v-if="column.type === 'text'"
                  :id="column.id"
                  type="text"
                  v-model="selectedChoices[column.id]"
                  class="text-input"
              />
            </template>
          </div>
        </div>
        <div class="button-container">
          <button class="btn-submit" @click="submitReview()">Submit Review</button>
          <button class="btn-reject" @click="rejectReview()">Reject Article</button>
        </div>
      </div>



    </div>
  </div>
</template>


<script>
// Import Axios
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
        // console.log(response.data);  // Debugging line to see the actual structure of the response
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
  align-items: stretch;
}

.form-table {
  flex: 1 1 auto;
  display: grid;
  grid-template-columns: auto auto;
  gap: 10px 20px;
  border: 1px solid #ccc;
  padding: 30px;
  border-radius: 10px;
  max-width: 800px;
  background-color: #ffffff;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

.article-section {
  height: auto;
  max-height: 300px;
  flex: 1 1 auto; /* Flex space for the article */
  padding: 10px 10px 10px 20px;
  border: 1px solid #ccc;
  border-radius: 10px;
  background-color: #ffffff;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  max-width: 500px;
  margin-left: 20px;
}

.form-row {
  display: contents;
}

.form-label {
  font-size: 16px;
  font-weight: 500;
  align-self: center;
}

.form-input {
  width: 100%;
}

.dropdown,
.text-input {
  width: 100%;
  max-width: 300px;
  padding: 10px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 5px;
  background-color: #fff;
  box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.1);
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
  margin-top: 20px;
  width: 100%;
}
</style>

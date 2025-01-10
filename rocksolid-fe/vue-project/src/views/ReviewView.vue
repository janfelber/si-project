<template>
  <div>
    <h1>Grading</h1>
    <div v-if="loading">Loading...</div> <!-- Loading state -->
    <div v-for="column in columns" :key="column.id">
      <label :for="column.id">{{ column.column_name }}</label>
      <template v-if="column.type === 'dropdown'">
        <select :id="column.id" v-model="selectedChoices[column.id]" style="padding: 8px; font-size: 14px; border-radius: 5px; border: 1px solid #ccc; width: 100%;"
        >
          <option v-for="choice in column.choices" :key="choice.id" :value="choice.id">{{ choice.choice_name }}</option>
        </select>
      </template>
      <template v-else>
        <input v-if="column.type === 'text'" :id="column.id" type="text" v-model="selectedChoices[column.id]" />
      </template>
    </div>
    <button @click="submitReview()">Submit Review</button> <!-- Submit Button -->


  </div>

  <div>
    <button @click="rejectReview()">Reject Article</button> <!-- Submit Button -->
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
        console.log(this.selectedChoices)
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
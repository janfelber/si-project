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
      loading: true  // Track loading state
    };
  },
  mounted() {
    this.fetchColumns();
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
    }
  }
};
</script>
<template>

</template>
<script>
import axios from 'axios';

export default {
  name: 'ArticleReviewResponse',
  props: ['id'],
  data() {
    return {
      headers: ["ID", "Názov", "Autor", "Stav", "Akcia"],
      articles: []
    }
  },
  methods: {
    async getArticles() {
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get(`http://localhost:8080/api/v1/review/article/${this.id}`,
            {
              headers: {
                Authorization: `Bearer ${token}`
              }
            });
        // this.articles = response.data;
        console.log(response.data)
      } catch (error) {
        console.error(error);
      }
    },
  },
  mounted() {
    this.getArticles();
  },
}
</script>
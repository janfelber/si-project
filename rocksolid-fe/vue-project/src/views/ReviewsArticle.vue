<template>
  <v-app>
    <v-main>
      <div class="filter-header">
        <h4 style="font-size: 20px"> Filter </h4>
      </div>
      <div class="filter-container">
        <div class="form-group">
          <label>Vyhladavacia fraza</label>
          <div class="form-input">
            <input v-model="searchQuery" placeholder="Zadajte vyhľadávací výraz">
          </div>
        </div>
        <div class="form-group filter-button">
          <button class="btn btn-primary filter" @click="filter" style="color: white">Filter</button>
          <button class="btn btn-primary reset" @click="reset" style="color: #1EB386">Resetovat</button>
        </div>
      </div>

      <div class="articles" style="padding: 0 1.5rem">
      <v-row class="pa-4" align="stretch" justify="start">
        <v-col
            cols="12"
            sm="6"
            md="4"
            lg="3"
            v-for="article in filteredArticles"
            :key="article.id"
        >
          <v-card class="elevation-3">
            <div class="card-header-green"></div>

            <v-card-title class="text-h6 font-weight-bold">
              {{ article.title }}
            </v-card-title>
            <v-card-subtitle class="text-body-2 grey--text">
              {{ article.date }}
            </v-card-subtitle>
            <v-divider></v-divider>
            <v-card-text class="py-3 text-truncate">
              {{ article.description }}
            </v-card-text>
            <v-card-actions class="justify-end">
              <v-btn
                  color="green darken-1"
                  class="white--text"
                  @click="reviewArticle(article.id)"
              >
                Recenzovať
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-col>
      </v-row>
      </div>
    </v-main>
  </v-app>
</template>

<script>
export default {
  data() {
    return {
      searchQuery: '',
      articles: [
        { id: 1, title: 'Article 1', description: 'Description for article 1.', date: '2025-01-04' },
        { id: 2, title: 'Article 2', description: 'Description for article 2.', date: '2025-01-02' },
        { id: 3, title: 'Article 3', description: 'Description for article 3.', date: '2025-01-03' },
        { id: 4, title: 'Article 4', description: 'Description for article 4.', date: '2025-01-01' },
        { id: 5, title: 'Article 5', description: 'Description for article 5.', date: '2025-01-05' },
      ],
      filteredArticles: []
    };
  },
  mounted() {
    this.filteredArticles = this.articles;
  },
  methods: {
    filter() {
      this.filteredArticles = this.articles.filter(article => {
        return article.title.toLowerCase().includes(this.searchQuery.toLowerCase());
      });
    },

    reset() {
      this.searchQuery = '';
      this.filteredArticles = this.articles;
    },

    reviewArticle(id) {
      alert(`Otvoriť článok č. ${id} na recenzovanie`);
    }
  }
};
</script>

<style scoped>
.card-header-green {
  height: 5px;
  background-color: #4caf50;
  border-top-left-radius: 4px;
  border-top-right-radius: 4px;
}
.v-card {
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}
.v-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.15);
}
.v-btn {
  border-radius: 20px;
  text-transform: none;
}
.v-card-title {
  color: #1b5e20;
}

.filter-header {
  align-items:      center;
  border-bottom:    1px solid #d8d8f0;
  background-color: rgba(178, 224, 217, 0.17);
  display:          flex;
  padding:          .75rem 2.5rem;

}

.filter-container {
  border-bottom: 1px solid #d8d8f0;
  padding: .75rem 2.5rem;
}

.form-group {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  margin-bottom: .75rem;
}

.form-input input {
  border: 1px solid #d8d8f0;
  padding: 8px;
  width: 40%;
  box-sizing: border-box;
  font-size: 0.75rem;
}

label {
  width: 11rem;
  align-self: center;
  font-size: 14px;
}

.form-input {
  flex: 1;
}

.filter-button {
  display: flex;
  gap: 10px;
  justify-content: flex-start;
  margin-top: 4rem;
}

.filter-button .filter {
  background-color: #1EB386;
  border-color: #1EB386;
}

.filter-button .reset {
  background-color: transparent;
  border-color: #1EB386;
}

.filter-button .filter,
.filter-button .reset {
  font-weight: bold;
  font-size: 14px;
  padding: 6px 12px;
}

.form-input input:focus {
  border: 1px solid #1EB386;
  outline: none;
}
</style>
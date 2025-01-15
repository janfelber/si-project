

<template >
  <v-app>

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

      <div class="card-container">
        <div class="v-col-7">
          <div class="card">
            <v-card-title class="card-title">O práci
            </v-card-title>
            <div class="card-body">
              <div class="form-group">
                <label class="input-label">Meno</label>
                <div class="form-input">
                  <input v-model="firstName">
                </div>
              </div>
              <div class="form-group">
                <label class="input-label">Priezvisko</label>
                <div class="form-input">
                  <input v-model="lastName">
                </div>
              </div>
              <div class="form-group">
                <label class="input-label">Názov práce</label>
                <div class="form-input">
                  <input v-model="fileName">
                </div>
              </div>
              <hr>
              <div class="form-group">
                <label class="input-label">Popis práce</label>
                <div class="form-input">
                  <input v-model="articleDescription">
                </div>
              </div>
              <div class="form-group">
                <label class="input-label">Klúčove slová</label>
                <div class="form-input">
                  <input v-model="keyWords">
                </div>
              </div>
              <div class="form-group">
                <label class="input-label">Spolu autori</label>
                <div class="form-input">
                  <input v-model="coAuthors" @input="removeTrailingComma">
                </div>
              </div>
              <div class="form-group">
                <label class="input-label" for="section">Vyberte sekciu:</label>
                <div class="form-input">
                  <select v-model="selectedOption">
                    <option v-for="section in sections" :key="section.id" :value="section.id" :selected="section.id === selectedOption">
                      {{ section.sectionName }}
                    </option>
                  </select>
                </div>
              </div>
            </div>
          </div>
          <div class="button-section">
            <button type="button" class="btn btn-primary save-button" @click="updateFile()">Uložiť</button>
          </div>
        </div>
        <div class="v-col-7">
          <div class="card">
            <v-card-title class="card-title">Nahrať súbor
            </v-card-title>
            <div class="card-body">
              <div class="upload-info">
                <p class="p">Nahratý súbor musi byť vo formáte .doc alebo .docx</p>
                <div v-if="word_file" class="form-group">
                  <label class="input-label">Názov súboru Word</label>
                  <div class="form-input">
                    <input v-model="word_file.name" disabled>
                  </div>
                </div>
              </div>
              <div class="upload-container"
                   @dragover.prevent="onDragOver"
                   @drop.prevent="onFileDropped">
                    <span class="icon-text">
                      <i class="upload-icon fas fa-upload"></i> Presuňte súbor alebo,
                    </span>
                <label for="file-upload" class="upload-label">
                  vyberte súbor
                </label>
                <input type="file" id="file-upload" class="file-input" accept=".doc,.docx" @change="onWordFileSelected"/>
              </div>
              <br>
              <p class="p">Nahratý súbor musi byť vo formáte .pdf</p>
              <div v-if="pdf_file" class="form-group">
                <label class="input-label">Názov súboru PDF</label>
                <div class="form-input">
                  <input v-model="pdf_file.name" disabled>
                </div>
              </div>
              <div class="upload-container"
                   @dragover.prevent="onDragOver"
                   @drop.prevent="onPdfFileDropped">
                    <span class="icon-text">
                      <i class="upload-icon fas fa-upload"></i> Presuňte PDF súbor alebo,
                    </span>
                <label for="pdf-file-upload" class="upload-label">
                  vyberte PDF súbor
                </label>
                <input type="file" id="pdf-file-upload" class="file-input" accept=".pdf" @change="onPdfFileSelected"/>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

  </v-app>
</template>

<script>
import axios from 'axios';

export default {
  name: 'UpdateFile',
  props: ['id', 'review_id'],
  data() {
    return {
      firstName: '',
      lastName: '',
      coAuthors: '',
      articleDescription: '',
      keyWords: '',
      word_file: null,
      pdf_file: null,
      fileName: '',
      selectedOption: null,
      conferenceId: this.id,
      userInConference: null,
      articleInReview: null,
      sections: [],
      section: null,
      sectionId: '',
      sectionName: '',
      dateFrom: "",
      dateTo: "",
      currentDate: new Intl.DateTimeFormat('en-CA').format(new Date()),
      alert_show: false,
      alert_text: "",
      alert_icon: "",
      alert_color: "",
      exists: false,
      review_id: null,
      articleReviewed: null,
    };
  },
  methods: {
    async sendUserToReview(reviewId) {
      this.$router.push({ name: 'ArticleReviewResponse', params: { review_id: reviewId }});
    },
    async fetchSections() {
      try {
        const token = localStorage.getItem("token");

        const response = await axios.get(
            `http://localhost:8080/api/v1/category/categories`,
            {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
        );
        this.sections = response.data;
        this.sections.forEach(section => {
          this.section = `${section.sectionId} ${section.sectionName}`;
        })
        console.log(this.sections);

      } catch (error) {
        console.error("Error checking user :", error);
      }
    },
    async getUser() {
      try {
        const token = localStorage.getItem("token");

        const response = await axios.get(
            `http://localhost:8080/api/v1/user/me`,
            {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
        );
        this.userId = response.data.id;
        this.firstName = response.data.first_name;
        this.lastName = response.data.last_name;
        console.log(response.data)
      } catch (error) {
        console.error("Error checking user :", error);
      }
    },
    async getDate() {
      try {
        const token = localStorage.getItem("token");

        const response = await axios.get(
            "http://localhost:8080/api/v1/conference/" + this.id,
            {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
        );
        this.dateTo = response.data.date_to;
        this.dateFrom = response.data.date_from;
      } catch (error) {
        console.error(error);
      }
    },
    onWordFileSelected(event) {
      const file = event.target.files[0];
      if (file) {
        if (this.isValidFile(file)) {
          this.word_file = file;
        } else {
          alert("Please select a .doc or .docx file.");
        }
      }
    },
    onPdfFileSelected(event) {
      const file = event.target.files[0];
      if (file) {
        if (this.isValidPdfFile(file)) {
          this.pdf_file = file;
        } else {
          alert("Please select a .pdf file.");
        }
      }
    },

    onDragOver(event) {
      event.preventDefault();
    },

    onFileDropped(event) {
      const file = event.dataTransfer.files[0];
      if (file) {
        if (this.isValidFile(file)) {
          this.word_file = file.name;
          this.word_file = file;
        } else {
          alert("Nahratý súbor musi byť vo formáte .doc alebo .docx");
        }
      }
    },
    onPdfFileDropped(event) {
      const file = event.dataTransfer.files[0];
      if (file) {
        if (this.isValidPdfFile(file)) {
          this.pdf_file = file.name;
          this.pdf_file = file;
        } else {
          alert("Nahratý súbor musi byť vo formáte .doc alebo .docx");
        }
      }
    },
    isValidFile(file) {
      const allowedExtensions = ['.doc', '.docx'];
      const fileExtension = file.name.split('.').pop().toLowerCase();
      return allowedExtensions.includes(`.${fileExtension}`);
    },
    isValidPdfFile(file) {
      const allowedExtensions = ['.pdf'];
      const fileExtension = file.name.split('.').pop().toLowerCase();
      return allowedExtensions.includes(`.${fileExtension}`);
    },
    async getArticleInfo() {
      try {
        const token = localStorage.getItem("token");

        const response = await axios.get(
            `http://localhost:8080/api/v1/article/student/${this.id}`,
            {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
        );
        this.article = response.data;
        this.fileName = response.data.articleName;
        this.coAuthors = response.data.coAuthors;
        this.articleDescription = response.data.articleDescription;
        this.keyWords = response.data.keyWords;
        const matchedSection = this.sections.find(section => section.sectionName === this.article.section);
        if (matchedSection) {
          this.selectedOption = matchedSection.id;
        }
      } catch (error) {
        console.error("Error deleting review:", error);
      }
    },
    async updateFile() {
      if (!this.firstName || !this.lastName || !this.fileName || !this.selectedOption || !this.coAuthors || !this.articleDescription || !this.keyWords) {
        alert('Prosím, vyplňte všetky údaje');
        return;
      }

      if (!this.word_file || !this.pdf_file) {
        alert('Prosím, vyberte súbor.');
        return;
      }

      if (this.currentDate < this.dateFrom || this.currentDate > this.dateTo) {
        alert("Do tejto konferencie momentálne nieje možné odovzdať prácu.");
        return;
      }

      const formData = new FormData();
      formData.append('wordFile', this.word_file);
      formData.append('pdfFile', this.pdf_file);
      formData.append('fileName', this.fileName);
      formData.append('coAuthors', this.coAuthors);
      formData.append('articleDescription', this.articleDescription);
      formData.append('keyWords', this.keyWords);
      formData.append('sectionId', this.selectedOption);
      formData.append('firstName', this.firstName);
      formData.append('lastName', this.lastName);
      formData.append('conferenceId', this.conferenceId);
      formData.append('articleId', this.id); // Assume this.id contains the existing article ID

      try {
        const token = localStorage.getItem("token");
        const response = await axios.patch("http://localhost:8080/api/v1/file/updateArticle", formData, {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'multipart/form-data'
          }
        });

        if (response.status === 200) {
          await this.showAlert("success");
          // Reset form fields after successful submission
          this.firstName = '';
          this.lastName = '';
          this.file = null;
          this.fileName = '';
          this.selectedOption = '';
          this.coAuthors = '';
          this.articleDescription = '';
          this.keyWords = '';
          this.articleInReview = true;
        }

        await this.deleteReview(); // Assuming this function is needed after update

      } catch (error) {
        console.error("Chyba pri nahrávaní súboru", error);
        this.showAlert("error");
      }
    },
    async getReview() {
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get(`http://localhost:8080/api/v1/review/getReviewByArticleId/${this.id}`,
            {
              headers: {
                Authorization: `Bearer ${token}`
              }
            });

        console.log(response.data)
        this.review_id = response.data.reviewId
        console.log("review id", this.review_id)
        this.reviewDetails = response.data.reviewDetails
      } catch (error) {
        console.error(error);
      }
    },
    async deleteReview() {
      try {
        const token = localStorage.getItem("token");

        const response = await axios.delete(
            `http://localhost:8080/api/v1/review/deleteReview/${this.review_id}`,
            {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
        );
        console.log("Review deleted successfully:", response.data);
      } catch (error) {
        console.error("Error deleting review:", error);
      }
    },
    async showAlert(status){
      if(status === "success"){
        this.alert_show = true;
        this.alert_text = "Práca bola úspešne nahraná";
        this.alert_icon = "$success";
        this.alert_color = "success";
        await new Promise(resolve => setTimeout(resolve, 4000));
        this.alert_show = false;
      }
      else if (status === "error"){
        this.alert_show = true;
        this.alert_text = "Prácu sa nepodarilo nahrať";
        this.alert_icon = "$error";
        this.alert_color = "error";
        await new Promise(resolve => setTimeout(resolve, 4000));
        this.alert_show = false;
      }
    },
  },
  mounted() {
    // console.log(this.articleInReview)
    const conferenceName = this.$route.query.conferenceName;
    this.getUser();
    this.getReview()
    this.fetchSections();
    this.getDate();
    this.getArticleInfo();
  }
}
</script>

<style scoped>

.button-section {
  display:    flex;
  margin-top: 1rem;
}

.save-button {
  color: white;
  background-color: #3c8d40;
  border-color: #3c8d40;
}

.form-group {
  display:       flex;
  flex-wrap:     wrap;
  align-items:   center;
  margin-bottom: .75rem;
}

.form-input input {
  border:     1px solid #d8d8f0;
  padding:    8px;
  width:      100%;
  box-sizing: border-box;
}

.form-input {
  flex: 1;
}

.input-label {
  width:      10rem;
  align-self: center;
}

.card-title {
  font-weight:   500;
  font-size:     17px;
  border-bottom: 1px solid #d8d8f0;
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

.card-container {
  display: flex;
}

.card {
  display:          flex;
  flex-direction:   column;
  min-width:        0;
  position:         relative;
  word-wrap:        break-word;
  background-clip:  initial;
  background-color: #fefefe;
  border:           1px solid #d8d8f0;
  border-radius:    .5rem;
}

.card-body {
  min-height: 1px;
  padding:    .75rem;
}

.v-col-7 {
  flex: 1;
  max-width: 48%;
}

hr {
  border:        0;
  border-top:    1px solid darkgrey;
  margin-bottom: .75rem;
  margin-top:    .75rem;
}

.upload-container {
  border: 2px dashed darkgreen;
  border-radius: 10px;
  padding: 30px;
  text-align: center;
  color: #6c757d;
  background-color: #f8f9fa;
  transition: background-color 0.3s ease, border-color 0.3s ease;
}

.upload-container:hover {
  background-color: #e9ecef;
  border-color: #3c8d40;
}

.file-input {
  display: none;
}

.upload-label {
  cursor: pointer;
  font-size: 16px;
  color: darkgreen;
  text-decoration: none;
}

.upload-label:hover {
  color: #3c8d40;
  text-decoration: underline;
}


.icon-text {
  align-items: center;
  gap: 8px;
  font-size: 16px;
}

.upload-icon {
  font-size: 18px;
  color: #3c8d40;
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



.v-container {
  background-color: #f9f9f9;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}


.v-text-field,
.v-file-input {
  margin-bottom: 20px;
}


.v-btn {
  background-color: #3f51b5;
  color: white;
  font-weight: bold;
  font-size: 1rem;
  transition: background-color 0.3s ease;
}

.v-btn:hover {
  background-color: #283593;
}


.v-radio-group {
  margin-top: 20px;
}

.v-radio {
  --v-theme-primary: #3f51b5;
  margin-right: 10px;
}


.p {
  font-size: 1rem;
  color: #555;
  margin-top: 10px;
}


.v-divider {
  margin: 20px 0;
  border-color: #e0e0e0;
}


@media (max-width: 768px) {
  h1 {
    font-size: 2rem;
  }

  .v-container {
    padding: 15px;
  }

  .v-btn {
    font-size: 0.9rem;
  }
}
</style>
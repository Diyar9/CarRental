import axios from 'axios';

// Bas-URL för REST API som hanterar uthyrningar
const REST_API_BASE_URL = 'http://localhost:8080/api/rentals';

//Hämtar en lista över uthyrningar från API:t. @returns {Promise} - En Promise som löser sig till svaret från API:t.
export const listRentals = () => axios.get(REST_API_BASE_URL);

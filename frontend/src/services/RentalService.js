import axios from 'axios';

const REST_API_BASE_URL = 'http://localhost:8080/api/rentals';

export const listRentals = () => axios.get(REST_API_BASE_URL);
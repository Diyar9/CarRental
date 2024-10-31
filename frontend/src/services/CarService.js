import axios from 'axios';

const REST_API_BASE_URL = 'http://localhost:8080/api/cars';

export const listCars = () => axios.get(REST_API_BASE_URL);
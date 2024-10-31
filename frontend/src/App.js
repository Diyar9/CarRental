import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import NavbarComponent from './components/NavbarComponent';
import RentCarFormComponent from './components/RentCarFormComponent';
import AdminOverviewComponent from './components/AdminOverviewComponent';
import { Container } from 'react-bootstrap';

function App() {
	return (
		<Router>
			<div className="App">
				<NavbarComponent />
				<Container className="mt-4">
					<Routes>
						<Route path="/rent" element={<RentCarFormComponent />} />
						<Route path="/admin" element={<AdminOverviewComponent />} />
					</Routes>
				</Container>
			</div>
		</Router>
	);
}

export default App;
import React from 'react';
import { Navbar, Container, Nav, Button } from 'react-bootstrap';
import { Link } from 'react-router-dom';

const NavbarComponent = () => {
    return (
        <Navbar expand="lg" className="custom-navbar">
            <Container>
                <Navbar.Brand as={Link} to="/rent">Fortnox Car Rental</Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className="me-auto">
                        <Nav.Link as={Link} to="/rent">Biluthyrning</Nav.Link>
                        <Nav.Link as={Link} to="/admin">Översikt</Nav.Link>
                    </Nav>
                    <img
                        src="https://static.vecteezy.com/system/resources/previews/035/915/849/non_2x/ai-generated-car-logo-isolated-no-background-ai-generated-free-png.png"
                        alt="Fortnox Logo"
                        style={{width: '70px', height: '30px'}}
                    />
                </Navbar.Collapse>
            </Container>
        </Navbar>
    );
};

export default NavbarComponent;

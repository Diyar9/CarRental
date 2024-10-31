export const validateDriverName = (name) => {
    if (!/^[a-zA-Z\s]+$/.test(name.trim())) {
        return 'Förarnamn får endast innehålla bokstäver och mellanslag.';
    }
    return null;
};

export const validateDriverAge = (driverAge) => {
    if (parseInt(driverAge) < 18) {
        return 'Föraren måste vara minst 18 år.';
    }
    return null;
};

export const validateRentalDates = (pickUpDate, returnDate) => {
    const today = new Date();
    today.setHours(0, 0, 0, 0);  // Sätter tid till 00:00 för att endast jämföra datum

    if (new Date(pickUpDate) < today) {
        return 'Uthyrningsdatum kan inte vara i det förflutna.';
    }

    if (new Date(pickUpDate) >= new Date(returnDate)) {
        return 'Returdatum måste vara efter uthyrningsdatum.';
    }

    return null;
};


export const validateRequiredFields = (selectedCar, pickUpDate, returnDate, driverName, driverAge) => {
    if (!selectedCar || !pickUpDate || !returnDate || !driverName || !driverAge) {
        return 'Alla fält måste vara ifyllda.';
    }
    return null;
};

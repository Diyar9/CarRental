export const fetchCars = async () => {
    const response = await fetch('/cars');
    if (!response.ok) throw new Error('Failed to fetch cars');
    return response.json();
};

export const fetchRentals = async () => {
    const response = await fetch('/rentals');
    if (!response.ok) throw new Error('Failed to fetch rentals');
    return response.json();
};

export const rentCar = async (rental) => {
    const response = await fetch('/rentals', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(rental),
    });
    if (!response.ok) throw new Error('Failed to rent car');
};

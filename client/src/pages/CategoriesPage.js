import React, { useEffect, useState } from 'react';
import axios from 'axios';

const CategoriesPage = () => {

	const [categories, setCategories] = useState(null);
	useEffect(() => {
		const getMessage = async () => {
			const result = await axios.get('/xD', { validateStatus: false });
			console.log(result);

			if (result.status !== 200) {
				alert('Some error occured');
				return;
			}
			setCategories(result.data);
		};

		getMessage();
	}, []);

	if (!categories) return <p>This is categories page</p>;
	else return (
		<>
			<ul>
				{
					categories.map((c, i) => <li key={`cat-${i}`}>id: {c.id} | name: {c.name}</li>)
				}
			</ul>
		</>
	);
};

export default CategoriesPage;
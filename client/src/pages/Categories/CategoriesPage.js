import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';

const CategoriesPage = () => {
	const [state, setState] = useState({ loading: true, categories: null });
	useEffect(() => {
		const getMessage = async () => {
			const result = await axios.get('/api/categories', { validateStatus: false });
			if (result.status !== 200) {
				alert('Some error occured');
				return;
			}
			setState({ loading: false, categories: result.data });
		};

		getMessage();
	}, []);

	if (state.loading) return <></>
	else if (!state.categories || state.categories.length === 0) return <h3>No categories found</h3>
	else return (
		<>
			<p>Categories:</p>
			<ul>
				{
					state.categories.map((c, i) =>
						<Link to={`/categories/${c.id}`}>
							<li key={`cat-${i}`}>
								{c.name}
							</li>
						</Link>
					)
				}
			</ul>
		</>
	);
};

export default CategoriesPage;
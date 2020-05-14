import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Link, useHistory } from 'react-router-dom';

const CategoriesPage = () => {
	const history = useHistory();

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
						<Link to={`/categories/${c.id}`} key={`link-${i}`}>
							<li key={`cat-${i}`}>
								{c.name}
							</li>
						</Link>
					)
				}
			</ul>
			<button type="button" className="btn btn-success w-25"
				onClick={() => history.push('/categories/create')}>
				Create
			</button>
		</>
	);
};

export default CategoriesPage;
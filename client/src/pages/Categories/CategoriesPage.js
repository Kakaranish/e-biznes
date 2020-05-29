import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';
import { doRequest } from '../../common/Utils';

const CategoriesPage = () => {

	const [state, setState] = useState({ loading: true, categories: null });

	useEffect(() => {
		const getMessage = async () => {
			try {
				const action = async () => axios.get('/api/categories', { validateStatus: false });
				const result = await doRequest(action);
				setState({ loading: false, categories: result });
			} catch (error) {
				alert(`${error} error occured`);
			}
		};
		getMessage();
	}, []);

	if (state.loading) return <></>
	else if (!state.categories || state.categories.length === 0) return <>
		<h3>No categories found</h3>
		<Link to={{ pathname: '/manage/categories/create' }} className="btn btn-success w-25">
			Add First Category
		</Link>
	</>
	else return (
		<>
			<h3>Product categories:</h3>
			<ul>
				{
					state.categories.map((c, i) =>
						<Link to={`/products/category/${c.id}`} key={`link-${i}`}>
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
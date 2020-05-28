import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';
import { doRequest } from '../../../Utils';

const CategoriesPage = () => {

	const [state, setState] = useState({ loading: true, categories: null });

	useEffect(() => {
		const getMessage = async () => {
			const action = async () => axios.get('/api/categories', { validateStatus: false });
			
			try {
				const result = await doRequest(action);
				setState({ loading: false, categories: result });
			} catch (error) {
				alert(error.msg);
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
			<p>Categories:</p>
			<ul>
				{
					state.categories.map((c, i) =>
						<Link to={`/manage/categories/${c.id}`} key={`link-${i}`}>
							<li key={`cat-${i}`}>
								{c.name}
							</li>
						</Link>
					)
				}
			</ul>
			<Link to={{ pathname: '/manage/categories/create' }} className="btn btn-success w-25">
				Create
			</Link>
		</>
	);
};

export default CategoriesPage;
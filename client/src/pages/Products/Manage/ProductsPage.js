import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';

const ProductsPage = () => {

	const [state, setState] = useState({ loading: true, products: null });
	useEffect(() => {
		const fetchProducts = async () => {
			const result = await axios.get('/api/products', { validateStatus: false });
			if (result.status !== 200) {
				setState({ loading: false, products: null });
				alert('Some error occured');
				console.log(result);
				return;
			}
			console.log(result);
			setState({ loading: false, products: result.data.map(p => p.product) });
		};
		fetchProducts();
	}, []);

	if (state.loading) return <></>
	else {
		if (!state.products || !state.products.length) return <>
			<h3>There are no products to show</h3>
			<Link to={{ pathname: '/manage/products/create' }} className="btn btn-success w-25">
				Add First Product
			</Link>	
		</>
		
		else return <>
			<ul>
				{
					state.products.map((prod, i) =>
						<Link to={`/manage/products/${prod.id}`} key={`link-${i}`}>
							<li key={`prod-${i}`}>
								{prod.name}
							</li>
						</Link>
					)
				}
			</ul>

			<Link to={{ pathname: '/manage/products/create' }} className="btn btn-success w-25">
				Create
			</Link>
		</>
	}
};

export default ProductsPage;
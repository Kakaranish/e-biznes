import React from 'react';
import axios from 'axios';
import { useHistory } from 'react-router-dom';
import { doRequest } from '../../../common/Utils';
import AwareComponentBuilder from '../../../common/AwareComponentBuilder';
import Modal from '../../../components/Modal';

const Opinion = (props) => {

	const history = useHistory();
	const opinionInfo = props.opinionInfo;

	const deleteOpinionOnClick = async opinionId => {
		try {
			const action = async () => axios.delete('/api/admin/opinions', {
				data: { opinionId: opinionId },
				validateStatus: false,
				headers: { 'X-Auth-Token': props.auth.token }
			});
			await doRequest(action);

			history.go();
		} catch (error) {
			alert(`${error} error occured`);
		}
	}

	return <>
		<div className="p-3 mb-2" style={{ border: "1px solid gray" }} key={`o-${opinionInfo.opinion.id}`}>
			<p className="mb-1">
				<b>{opinionInfo.user.firstName} {opinionInfo.user.lastName}</b> ({opinionInfo.user.email}) wrote:
			</p>

			<p>{opinionInfo.opinion.content}</p>

			{
				props.auth?.role === 'ADMIN' &&
				<Modal title={"Are you sure"}
					btnText={"Delete"}
					btnClasses={"btn btn-danger w-25"}
					modalTitle={"Are you sure?"}
					modalPrimaryBtnText={"Delete"}
					modalPrimaryBtnClasses={"btn btn-danger"}
					onModalPrimaryBtnClick={async () => deleteOpinionOnClick(opinionInfo.opinion.id)}
					modalSecondaryBtnText={"Cancel"}
					modalSecondaryBtnClasses={"btn btn-secondary"}
				/>
			}
		</div>
	</>
};

export default new AwareComponentBuilder()
	.withAuthAwareness()
	.build(Opinion);
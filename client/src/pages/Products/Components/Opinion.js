import React from 'react';

const Opinion = ({ opinionInfo }) => {
	return <>
		<div className="p-3 mb-2" style={{ border: "1px solid gray" }} key={`o-${opinionInfo.opinion.id}`}>
			<p className="mb-1">
				<b>{opinionInfo.user.firstName} {opinionInfo.user.lastName}</b> ({opinionInfo.user.email}) wrote:
			</p>

			{opinionInfo.opinion.content}
		</div>
	</>
};

export default Opinion;